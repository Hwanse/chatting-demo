<script>
import SockJS from "sockjs-client"
import Stomp from "webstomp-client"

export default {
    data() {
        return {
            websocket: null,
            stompClient: null,
        }
    },
    methods: {
        joinChat() {
            this.websocket = new SockJS(`${location.protocol}//${location.host}/ws/chat`)
            this.stompClient = Stomp.over(websocket)

            this.promise(stompClient.connect(
                    {}, 
                    this.onConnected, 
                    error => console.log(error) 
                ))
                .then(this.monitoringUserCount())
                .catch(reason => console.log(reason))
        },
        onConnected() {
            stompClient.subscribe(`/sub/chat-room/${this.info.id}`, response => {
                let responseData = JSON.parse(response.body)
                
                if (responseData.messageType === "MONITOR") {
                    this.$emit("@monitoring", responseData.userCount)
                } else {
                    this.messages.push(responseData)
                }
                
                /**
                 * Scrollbar end로 조작 시 비동기 데이터를 받고 Dom이 그려진 이후에 동작시켜야 하기 때문에
                 * 아래와 같은 비동기 콜백 함수 안에 실행되도록 해야한다
                 */
                this.$nextTick(() => {
                    this.controlScroll()
                })
            })

            let data = this.getMessageObject("join", "JOIN")
            stompClient.send("/pub/chat/join", JSON.stringify(data))
        },
        sendMessage(message) {
            if (!message.trim()) return
            
            this.message = message
            let data = this.getMessageObject(this.message, "TALK")
            stompClient.send("/pub/chat/message", JSON.stringify(data))
            this.message = ''
        },
        getMessageObject(content, type) {
            return {
                roomId: this.info.id,
                message: content,
                sender: this.sender,
                messageType: type
            }
        },
    },
}
</script>