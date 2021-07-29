<template>
    <div class="container">
        <audio ref="localAudio" autoplay muted controls></audio>
    </div>
</template>

<script>
import SockJS from "sockjs-client"
import Stomp from "webstomp-client"

let websocket
let stompClient

window.onbeforeunload = async () => {
    let index = this.connections.indexOf(this.mySessionId)
    if (index > -1) {
        this.connections[this.mySessionId].close()
        this.connections.splice(index, 1)
    }

    await stompClient.send("/pub/chat/leave")
    this.checkVisitor.clearInterval()
    stompClient.disconnect()
}

export default {
    data() {
        return {
            mediaConfig: {
                audio: true
            },
            iceConfig: {
                "iceServers": [
                    {"urls": "stun:stun.l.google.com:19302"},
                    {"urls": "stun:stun1.l.google.com:19302"}
                ]       
            },
            myVoiceStream: null,
            mySessionId: null,
            connections: [],
            checkVisitor: null,
        }
    },
    async created() {
        await this.setupMyMedia()
        await this.joinChat()    
    },
    beforeDestroy() {
        this.checkVisitor.clearInterval()
        stompClient.disconnect()
        websocket.close()
    },
    methods: {
        async setupMyMedia() {
            try {
                this.myVoiceStream = await navigator.mediaDevices.getUserMedia(this.mediaConfig)
                this.$refs.localAudio.srcObject = this.myVoiceStream
                this.$refs.localAudio.muted = true
                this.$refs.localAudio.volume = 0
            } catch(error) {
                console.log("get media error occured : ", error)
            }
        },
        async joinChat() {
            websocket = new SockJS(`${location.protocol}//${location.host}/ws/chat`)
            // let options = {debug: false, protocols: Stomp.VERSIONS.supportedProtocols()};
            stompClient = Stomp.over(websocket)

            await stompClient.connect({}, this.onConnected, this.onConnectError)

            this.checkVisitor = setInterval(() => stompClient.send("/pub/chat/visitors"), 2000)
        },
        onConnected() {
            this.bindSessionId()
            stompClient.subscribe("/sub/chat-room/1/join", this.userJoinEvent)
            stompClient.subscribe(`/user/${this.mySessionId}/sub/chat-room/1/visitors`, this.checkVisitors)
            stompClient.subscribe(`/user/${this.mySessionId}/sub/chat-room/1/voice`, this.getMessageFromSignallingServer)
            stompClient.subscribe("/sub/chat-room/1/leave", this.handleLeavePeer)

            stompClient.send("/pub/chat/voice/join")
        },
        onConnectError(error) {
            console.log("stomp connect error : ", error)
        },
        createPeerConnection(sessionId, isReceiveOffer) {
            if (!this.connections[sessionId]) {
                let connection = new RTCPeerConnection(this.iceConfig)
                this.connections[sessionId] = connection
                
                for (const track of this.myVoiceStream.getTracks()) {
                    connection.addTrack(track)
                }

                connection.onicecandidate = () => this.handleIceCandidate(event, sessionId)
                connection.ontrack = () => this.handleRemoteTrackAdded(event, sessionId)
                connection.onremovetrack = () => this.handleRemovedTrack(event, sessionId)
                connection.onconnectionstatechange = () => this.handleDisconnected(event, sessionId)

                if (!isReceiveOffer) {
                    connection.addEventListener("negotiationneeded", async () => {
                        await this.createOfferToSignallingServer(connection, sessionId)
                    })
                }
            }
        },
        handleIceCandidate(event, sessionId) {
            if (event.candidate) {
                let data = {
                    type: "candidate",
                    fromId: this.mySessionId,
                    toId: sessionId,
                    candidate: event.candidate
                }
                stompClient.send("/pub/chat/candidate", JSON.stringify(data))
            } else {
                console.log('End of candidates.')
            }
        },
        handleRemoteTrackAdded(event, sessionId) {
            const container = document.getElementsByClassName("container")[0]
            const audio = document.createElement("audio")

            audio.setAttribute("id", sessionId)
            audio.srcObject = new MediaStream([event.track])
            audio.controls = true
            audio.autoplay = true

            container.appendChild(audio)
        },
        handleDisconnected(event, sessionId) {
            const connectionStatus = this.connections[sessionId].connectionState;
            if (["disconnected", "failed", "closed"].includes(connectionStatus)) {
                const audioElement = document.getElementById(sessionId)
                if (audioElement) {
                    audioElement.remove()
                }
                stompClient.send("/pub/chat/leave-noti", JSON.stringify({sessionId: sessionId}))
            }
        },
        handleLeavePeer(response) {
            const sessionId = JSON.parse(response.body).sessionId
            const audioElement = document.getElementById(sessionId)
            if (audioElement) {
                audioElement.remove()
            }
        },
        async createOfferToSignallingServer(connection, toId) {
            const offer = await connection.createOffer()  
            await connection.setLocalDescription(offer)

            let sdpMessage = this.getSdpMessage(connection, toId)
            stompClient.send("/pub/chat/offer", JSON.stringify(sdpMessage))
        },
        async createAnswerToSignallingServer(connection, toId) {
            const answer = await connection.createAnswer()    
            await connection.setLocalDescription(answer)

            let sdpMessage = this.getSdpMessage(connection, toId)
            stompClient.send("/pub/chat/offer", JSON.stringify(sdpMessage))
        },
        async getMessageFromSignallingServer(response) {
            let message = JSON.parse(response.body)
            const fromId = message.fromId

            if (!fromId || fromId === this.mySessionId) return
            
            if (message.type === "offer") {
                if (!this.connections[fromId]) {
                    this.createPeerConnection(fromId, true)
                }
                await this.connections[fromId].setRemoteDescription(message.sdp)    
                await this.createAnswerToSignallingServer(this.connections[fromId], fromId)
            }

            if (message.type == "answer") {
                await this.connections[fromId].setRemoteDescription(message.sdp)
            }

            if(message.type === "candidate" && message.candidate) {
                this.connections[fromId].addIceCandidate(new RTCIceCandidate(message.candidate))
            }
        },
        userJoinEvent(response) {
            let joinSessionId = JSON.parse(response.body).joinSessionId

            if (this.mySessionId !== joinSessionId) {
                this.createPeerConnection(joinSessionId, false)
            }
        },
        checkVisitors(response) {
            let visitors = JSON.parse(response.body).visitors
            let isDeleteFlags = []

            for (const sessionId in this.connections) {
                let index
                isDeleteFlags[sessionId] = true

                visitors.forEach(visitor => {
                    index = this.connections.indexOf(visitor.sessionId)
                    if (index > -1) isDeleteFlags[visitor.sessionId] = false
                })
 
                if (isDeleteFlags[sessionId])
                    this.connections.splice(index, 1)
            }
        },
        getSdpMessage(connection, toId) {
            return {
                fromId: this.mySessionId,
                toId: toId,
                type: connection.localDescription.type,
                sdp: connection.localDescription
            }
        },
        bindSessionId() {
            let regex = /\/ws\/chat\/[0-9]+\/(\w+)\.*/g
            let result = regex.exec(websocket._transport.url)
            if (result) {
                this.mySessionId = result[1] ? result[1] : null
            }
        },
    }
}
</script>