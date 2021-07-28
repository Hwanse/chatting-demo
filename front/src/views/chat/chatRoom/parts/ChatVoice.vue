<template>
    <div class="container">
        <!-- <audio ref="localVoice" autoplay controls ></audio> -->
        <video ref="localVoice" autoplay muted playinline controls ></video>
        <video ref="remoteVoice" autoplay muted playinline controls ></video>
        <!-- <audio ref="remoteVoice" autoplay controls></audio> -->
        <button @click="call()">call me</button>
    </div>
</template>

<script>
import SockJS from "sockjs-client"
import Stomp from "webstomp-client"

let websocket
let stompClient

export default {
    data() {
        return {
            config: {
                video: true,
                audio: true
            },
            myVoiceStream: null,
            mySessionId: null,
            peerConnection: null,
            connections: [],
        }
    },
    created() {
        this.setupMyMedia()
        this.joinChat()    
    },
    methods: {
        setupMyMedia() {
            navigator.mediaDevices.getUserMedia(this.config)
                    .then(this.getMyVoiceStream)
                    .catch(reason => console.log(reason))
        },
        getMyVoiceStream(stream) {
            this.myVoiceStream = stream
            this.$refs.localVoice.srcObject = stream

        },
        createPeerConnection(sessionId) {
            // try {
                if (!this.connections[sessionId]) {
                    let connection = new RTCPeerConnection()
                    this.connections[sessionId] = connection
                    
                    for (const track of this.myVoiceStream.getTracks()) {
                        connection.addTrack(track)
                    }

                    connection.onicecandidate = () => this.handleIceCandidate(event, sessionId)
                    connection.ontrack = () => this.handleRemoteStreamAdded(event, sessionId)
                    // connection.addEventListener("negotiationneeded", async () => {
                    //     this.createOfferToSignallingServer(connection)
                    // })
                }
            // } catch (error) {
            //     console.log("create peer error : ", error)
            // }
        },
        handleIceCandidate(event, sessionId) {
            if (event.candidate) {
                let data = {
                    type: "candidate",
                    fromId: sessionId,
                    candidate: event.candidate
                }
                stompClient.send("/pub/chat/candidate", JSON.stringify(data))
            } else {
                console.log('End of candidates.')
            }
        },
        // handleRemoteStreamAdded(event, sessionId) {
        handleRemoteStreamAdded(event) {
            // let container = document.getElementsByClassName("container")[0]
            // let video  = document.createElement("video")

        },
        createOfferToSignallingServer(connection) {
            connection.createOffer()
                    .then(offer => {
                        return connection.setLocalDescription(offer)
                    })
                    .then(() => {
                        let data = {
                            fromId: this.mySessionId,
                            type: connection.localDescription.type,
                            sdp: connection.localDescription
                        }
                        stompClient.send("/pub/chat/offer", JSON.stringify(data))
                    })
                    .catch(error => console.log("create offer : ", error))
        },
        createAnswerToSignallingServer(connection) {
            connection.createAnswer()
                .then(answer => {
                    return connection.setLocalDescription(answer)
                })
                .then(() => {
                    let data = {
                        fromId: this.mySessionId,
                        type: connection.localDescription.type,
                        sdp: connection.localDescription
                    }
                    stompClient.send("/pub/chat/offer", JSON.stringify(data))
                })
                // .catch(error => {
                //     console.log('Failed to create session description: ' + error.toString())
                // })
        },
        joinChat() {
            websocket = new SockJS(`${location.protocol}//${location.host}/ws/chat`)

            // let options = {debug: false, protocols: Stomp.VERSIONS.supportedProtocols()};
            stompClient = Stomp.over(websocket)
            stompClient.connect({}, this.onConnected, this.onConnectError)
        },
        onConnected() {
            this.bindSessionId()
            stompClient.subscribe("/sub/chat-room/1/visitors", this.userJoinEvent)
            stompClient.subscribe(`/sub/chat-room/1/voice`, this.getMessageFromSignallingServer)

            stompClient.send("/pub/chat/visitors")
        },
        onConnectError(error) {
            console.log("stomp connect error : ", error)
        },
        getMessageFromSignallingServer(response) {
            let message = JSON.parse(response.body)
            const fromId = message.fromId

            if (fromId === this.mySessionId) return

            switch (message.type) {
                case "offer":
                    console.log(fromId, this.mySessionId)
                    this.connections[this.mySessionId].setRemoteDescription(message.sdp)
                        .then(() => {
                            return this.createAnswerToSignallingServer(this.connections[this.mySessionId])
                        })
                    break
                case "answer":
                    this.connections[this.mySessionId].setRemoteDescription(message.sdp)
                    break
                case "candidate":
                    this.connections[this.mySessionId].addIceCandidate(new RTCIceCandidate(message.candidate))
                        .catch(error => console.log(error))
                    break
            }
        },
        bindSessionId() {
            let regex = /\/ws\/chat\/[0-9]+\/(\w+)\/websocket/g
            let result = regex.exec(websocket._transport.url)
            if (result) {
                this.mySessionId = result[1] ? result[1] : null
            }
        },
        userJoinEvent(response) {
            let joinedEventData = JSON.parse(response.body)
            
            joinedEventData.visitors.forEach(visitor => {
                this.createPeerConnection(visitor.sessionId)
            })

            // if (joinedEventData.visitors.length > 1) {
            //     this.createOfferToSignallingServer(this.connections[this.mySessionId])
            // }
        },
        call() {
            this.createOfferToSignallingServer(this.connections[this.mySessionId])
        }

    }
}
</script>