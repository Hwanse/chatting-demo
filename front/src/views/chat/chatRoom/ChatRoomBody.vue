<template>
    <div>
        <ChatInputNicknameDialog :isInputNickname="noInputNickname" v-on:@click="inputNickname"/>

        <v-card>
            <div class="chat-container" ref="chatBox">
                <ChatMessage v-for="(item, index) in messages" :key="index" :item="item" :sender="sender"/>    
            </div>
            
            <ChatMessageSendForm v-on:@send="sendMessage"/>
        </v-card>

        
    </div>
</template>

<script>
import SockJS from "sockjs-client"
import Stomp from "webstomp-client"
import ChatMessage from "./parts/ChatMessage.vue"
import ChatInputNicknameDialog from "./parts/ChatInputNicknameDialog.vue"
import ChatMessageSendForm from "./parts/ChatMessageSendForm.vue"

let websocket
let stompClient
let chatContainer

export default {
    props: ['info'],
    data() {
        return {
            message: '',
            messages: [],
            sender: '',
            noInputNickname: true,
            pollingUserCount: null,
        }
    },
    methods: {
        inputNickname(nickname) {
            if (!nickname.trim()) return

            this.sender = nickname
            this.noInputNickname = false
            this.joinChat()
        },
        joinChat() {
            websocket = new SockJS(`${location.protocol}//${location.host}/ws/chat`)
            stompClient = Stomp.over(websocket)

            this.promise(stompClient.connect({}, this.onConnected, this.onError))
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
                this.$nextTick(() => this.controlScroll())
            })

            let data = this.getMessageObject("join", "JOIN")
            stompClient.send("/pub/chat/join", JSON.stringify(data))
        },
        onError(error) {
            console.log(error)
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
        controlScroll() {
            chatContainer = this.$refs.chatBox
            let shouldScroll = chatContainer.scrollTop + chatContainer.clientHeight === chatContainer.scrollHeight

            if (!shouldScroll) this.scrollToEnd()
        },
        scrollToEnd() {
            chatContainer.scrollTop = chatContainer.scrollHeight
        },
        monitoringUserCount() {
            this.pollingUserCount = setInterval(() => {
                let data = this.getMessageObject(null, "MONITOR")
                stompClient.send("/pub/chat/monitoring", JSON.stringify(data))
            }, 2500)
        },
        promise(running) {
            return new Promise(() => running)
        }
    },
    beforeDestroy() {
        clearInterval(this.pollingUserCount)
    },
    components: {
        ChatMessage,
        ChatInputNicknameDialog,
        ChatMessageSendForm
    }
}
</script>

<style>
.chat-container {
    box-sizing: border-box;
    overflow-y: auto;
    padding: 0.5rem;
    height: calc(100vh - 9.5rem);
    min-height: 30rem;
}
.message-form {
    padding: 10px;
    min-height: 5.8rem;
}
.message {
    margin: 1ch 0;
}
.message.own {
    display: flex;
    justify-content: flex-end;
}
.message .content {
    display: inline-block;
    min-width: 1rem;
    max-width: 15rem;
    background-color: #ECEFF1;
    padding: 0.5rem;
}
.message.own .content{
    background-color: #E1F5FE;
}
.slider {
  height: 100%;
}
.slider-slide {
  /* color: #000; 
  background-color: #fff; text-align: center;  */
  font-family: "HelveticaNeue-Light", "Helvetica Neue Light", "Helvetica Neue", Helvetica, Arial, "Lucida Grande", sans-serif; 
  font-weight: 300; 
}
</style>