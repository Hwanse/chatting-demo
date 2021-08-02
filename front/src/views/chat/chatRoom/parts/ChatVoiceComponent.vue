<template>
    <div class="container">
        <audio ref="localAudio" autoplay muted controls></audio>
    </div>
</template>

<script>
export default {
    props: ["roomId", "inputNickname", "bus", "stompClient"],
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
        }
    },
    watch: {
        async inputNickname(nickname) {
            if (nickname) {
                this.inputNickname = nickname
                await this.setupMyMedia()
                this.mySessionId = this.bus.$data.mySessionId
            }
        }
    },
    mounted() {
        this.bus.$on("join", this.userJoinEvent)
        this.bus.$on("signalling", this.getMessageFromSignallingServer)
        this.bus.$on("leave", this.handleLeavePeer)
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
        createPeerConnection(sessionId, isReceiveOffer) {
            if (!this.connections[sessionId]) {
                let connection = new RTCPeerConnection(this.iceConfig)
                this.connections[sessionId] = connection

                for (const track of this.myVoiceStream.getTracks()) {
                    connection.addTrack(track)
                }

                connection.onicecandidate = event => this.handleIceCandidate(event, sessionId)
                connection.ontrack = event => this.handleRemoteTrackAdded(event, sessionId)
                connection.onconnectionstatechange = () => this.handleDisconnected(sessionId)

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
                    roomId: this.roomId,
                    type: "candidate",
                    fromId: this.mySessionId,
                    toId: sessionId,
                    candidate: event.candidate
                }
                this.stompClient.send("/pub/chat/voice/candidate", JSON.stringify(data))
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
        handleDisconnected(sessionId) {
            const connectionStatus = this.connections[sessionId].connectionState;
            if (["disconnected", "failed", "closed"].includes(connectionStatus)) {
                let leaveMessage = {
                    roomId: this.roomId,
                    sessionId: sessionId
                }
                this.stompClient.send("/pub/chat/voice/leave", JSON.stringify(leaveMessage))

                let index = this.connections.indexOf(sessionId)
                if (index > -1) {
                    this.connections.splice(index, 1)
                }
            }
        },
        handleLeavePeer(response) {
            const audioElement = document.getElementById(response.body)
            if (audioElement) {
                audioElement.remove()
            }
        },
        async createOfferToSignallingServer(connection, toId) {
            const offer = await connection.createOffer()  
            await connection.setLocalDescription(offer)

            let sdpMessage = this.getSdpMessage(connection, toId)
            this.stompClient.send("/pub/chat/voice/offer", JSON.stringify(sdpMessage))
        },
        async createAnswerToSignallingServer(connection, toId) {
            const answer = await connection.createAnswer()    
            await connection.setLocalDescription(answer)

            let sdpMessage = this.getSdpMessage(connection, toId)
            this.stompClient.send("/pub/chat/voice/offer", JSON.stringify(sdpMessage))
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
            let sessionId = JSON.parse(response.body).sessionId

            if (sessionId && this.mySessionId !== sessionId) {
                this.createPeerConnection(sessionId, false)
            }
        },
        getSdpMessage(connection, toId) {
            return {
                roomId: this.roomId,
                fromId: this.mySessionId,
                toId: toId,
                type: connection.localDescription.type,
                sdp: connection.localDescription
            }
        },
    }
}
</script>