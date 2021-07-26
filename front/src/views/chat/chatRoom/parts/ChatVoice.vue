<template>
    <div>
        <audio ref="localVoice" autoplay muted playinline></audio>
        <audio ref="remoteVoice" autoplay muted playinline></audio>
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
                audio: true
            },
            myVoiceStream: null,
            peerConnection: null,
        }
    },
    created() {
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
            
            // TODO send to Signalling server
            this.sendMessage("get user media")
            // 첫 연결 시작일때 RTCPeerConnection 초기화
            this.start(stream)
        },
        start(stream) {
            if (this.myVoiceStream) {
                this.createPeerConnection()
                // this.peerConnection.addStream(stream)
                for (const track of stream.getTracks()) {
                    this.peerConnection.addTrack(track)
                }
    
                // send offer to peer
                this.doCall()
            }
        },
        createPeerConnection() {
            try {
                this.peerConnection = new RTCPeerConnection(null)
                this.peerConnection.onicecandidate = this.handleIceCandidate
                // 아래 stream 관련 처리는 ios 환경에서 제한적이다 따라서 track 이벤트로 대체할 것을 권장
                // this.peerConnection.onaddstream = this.handleRemoteStreamAdded
                this.peerConnection.ontrack = this.handleRemoteStreamAdded
                this.peerConnection.onremovestream = this.handleRemoteStreamRemoved
            } catch (error) {
                console.log(error)
            }
        },
        handleIceCandidate(event) {
            if (event.candidate) {
                this.sendMessage(
                    {
                        type: 'candidate',
                        label: event.candidate.sdpMLineIndex,
                        id: event.candidate.sdpMid,
                        candidate: event.candidate.candidate
                    }
                )
            } else {
                console.log('End of candidates.')
            }
        },
        handleRemoteStreamRemoved(event) {
            console.log('Remote stream removed. Event: ', event)
        },
        handleRemoteStreamAdded(event) {
            console.log('Remote stream added.')
            this.$refs.remoteVoice = event.streams[0]
        },
        doCall() {
            this.peerConnection.createOffer(this.setLocalAndSendMessage, this.handleCreateOfferError)
        },
        setLocalAndSendMessage(sessionDescription) {
            this.peerConnection.setLocalDescription(sessionDescription)
            console.log('setLocalAndSendMessage sending message', sessionDescription)
            this.sendMessage(sessionDescription)
        },
        onCreateSessionDescriptionError(error) {
            trace('Failed to create session description: ' + error.toString())
        },
        handleCreateOfferError(event) {
            console.log('createOffer() error: ', event)
        },
        doAnswer() {
            console.log('Sending answer to peer.')
            pc.createAnswer()
                .then(this.setLocalAndSendMessage, this.onCreateSessionDescriptionError)
        },
        joinChat() {
            websocket = new SockJS(`${location.protocol}//${location.host}/ws/chat`)
            stompClient = Stomp.over(websocket)

            this.promise(stompClient.connect({}, this.onConnected, this.onError))
                .then(this.monitoringUserCount())
                .catch(reason => console.log(reason))
        },
        onConnected() {
            stompClient.subscribe(`/sub/chat-room/1`, response => {
                let responseData = JSON.parse(response.body)
                console.log(responseData)
                
                switch (responseData.type) {
                    case "OFFER":
                        break
                    case "ANSWER":
                        break
                    case "CANDIDATE":
                        break

                }

            })

            let data = this.getMessageObject("join", "JOIN")
            stompClient.send("/pub/chat/join", JSON.stringify(data))
        },
        onError(error) {
            console.log(error)
        },
        sendMessage() {
            stompClient.send("/pub/chat/message", JSON.stringify(data))
        },
        getMessageObject(content, type) {
            return {
                roomId: 1,
                message: content,
                messageType: type
            }
        }
    }
}
</script>