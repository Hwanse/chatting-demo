<template>
    <div class="container">
        <audio ref="localAudio" autoplay muted controls></audio>
    </div>
</template>

<script>
export default {
    props: ["bus"],
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
            connections: [],
            myVoiceStream: null,
            chatSetupData: {},
        }
    },
    mounted() {
        this.bus.$on("connect", this.startVoiceChat)
        this.bus.$on("join", this.userJoinEvent)
        this.bus.$on("signalling", this.getMessageFromSignallingServer)
        this.bus.$on("leave", this.handleLeavePeer)
        this.bus.$on("disconnect", () => this.closeMedia())
    },
    methods: {
        async startVoiceChat(setupData) {
            this.chatSetupData = setupData
            await this.setupMyMedia()
            await this.$nextTick()
            this.bus.$emit("connected")
        },
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
                    roomId: this.chatSetupData.roomId,
                    type: "candidate",
                    fromId: this.chatSetupData.mySessionId,
                    toId: sessionId,
                    candidate: event.candidate
                }
                this.chatSetupData.stompClient.send("/pub/chat/voice/candidate", JSON.stringify(data), this.getAuthorizationHeader())
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
                    roomId: this.chatSetupData.roomId,
                    sessionId: sessionId
                }
                this.chatSetupData.stompClient.send("/pub/chat/voice/leave", JSON.stringify(leaveMessage), this.getAuthorizationHeader())

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
            this.chatSetupData.stompClient.send("/pub/chat/voice/offer", JSON.stringify(sdpMessage), this.getAuthorizationHeader())
        },
        async createAnswerToSignallingServer(connection, toId) {
            const answer = await connection.createAnswer()    
            await connection.setLocalDescription(answer)

            let sdpMessage = this.getSdpMessage(connection, toId)
            this.chatSetupData.stompClient.send("/pub/chat/voice/offer", JSON.stringify(sdpMessage), this.getAuthorizationHeader())
        },
        async getMessageFromSignallingServer(response) {
            let message = JSON.parse(response.body)
            const fromId = message.fromId

            if (!fromId || fromId === this.chatSetupData.mySessionId) return 

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

            if (sessionId && this.chatSetupData.mySessionId && this.chatSetupData.mySessionId !== sessionId) {
                this.createPeerConnection(sessionId, false)
            }
        },
        getSdpMessage(connection, toId) {
            return {
                roomId: this.chatSetupData.roomId,
                fromId: this.chatSetupData.mySessionId,
                toId: toId,
                type: connection.localDescription.type,
                sdp: connection.localDescription
            }
        },
        closeMedia() {
            this.myVoiceStream.getTracks().forEach(track => track.stop())
        },
        getAuthorizationHeader() {
            const token = window.sessionStorage.getItem("authToken")
            return {"Authorization": `Bearer ${token}`}
        }
    }
}
</script>