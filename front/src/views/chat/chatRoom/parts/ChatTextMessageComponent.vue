<template>
    <div>
        <div id="messageConatiner" class="chat-container">
            <ChatMessage v-for="(item, index) in messages" :key="index" :item="item" :sender="sender"/>    
        </div>
        <ChatMessageSendForm v-on:@send="sendMessage"/>
    </div>
</template>

<script>
import ChatMessage from "./ChatMessage.vue"
import ChatMessageSendForm from "./ChatMessageSendForm.vue"

export default {
    props: ["roomId", "inputNickname", "bus", "stompClient"],
    data() {
        return {
            message: '',
            messages: [],
            sender: '',
        }
    },
    watch: {
        async inputNickname(nickname) {
            this.sender = nickname
            await this.$nextTick
        }
    },
    mounted() {
        this.bus.$on("join", this.handleMessage)
    },
    methods: {
        sendMessage(message) {
            if (!message.trim()) return
            
            this.message = message
            let data = this.getMessageObject(this.message, "TALK")
            this.stompClient.send("/pub/chat/text/message", JSON.stringify(data))
            this.message = ''
        },
        async handleMessage(response) {
            let responseData = JSON.parse(response.body)
                
            if (responseData.messageType === "MONITOR") {
                this.$emit("@monitoring", responseData.userCount)
            } else {
                this.messages.push(responseData)
                /**
                 * Scrollbar end로 조작 시 비동기 데이터를 받고 Dom이 그려진 이후에 동작시켜야 하기 때문에
                 * 아래와 같은 비동기 콜백 함수 안에 실행되도록 해야한다
                 */
                await this.$nextTick()
                this.controlScroll()
            }
        },
        getMessageObject(content, type) {
            return {
                roomId: this.roomId,
                message: content,
                sender: this.sender,
                messageType: type
            }
        },
        controlScroll() {
            let chatContainer = document.getElementById("messageConatiner")
            let shouldScroll = chatContainer.scrollTop + chatContainer.clientHeight === chatContainer.scrollHeight
            if (!shouldScroll) this.moveScrollEnd()
        },
        moveScrollEnd() {
            let chatContainer = document.getElementById("messageConatiner")
            chatContainer.scrollTop = chatContainer.scrollHeight
        },
    },
    components: {
        ChatMessage,
        ChatMessageSendForm,
    }
}
</script>

<style>
    .message-form {
        padding: 2%;
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
</style>