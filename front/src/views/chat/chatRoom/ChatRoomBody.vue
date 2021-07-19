<template>
    <div>
        <v-dialog v-model="noInputNickname" persistent max-width="500">
            <v-card class="pa-3">
                <v-row no-gutters>
                    <v-text-field
                        v-model="sender"
                        label="사용할 닉네임을 입력해주세요"
                        single-line
                        outlined
                        clearable
                        hide-details=""
                        class="mr-3"
                    ></v-text-field>
                    <v-btn x-large color="primary" @click="inputNickname">입장</v-btn>
                </v-row>
            </v-card>
        </v-dialog>

        <v-card>
            <div class="chat-container" ref="chatBox">
                <chat-message v-for="(item, index) in messages" :key="index" :item="item">
                    <div v-if="item.messageType === 'JOIN'" class="inline-block text-center rounded">
                        {{item.sender}}님이 입장하셨습니다.
                    </div>
                    <div v-if="item.messageType === 'TALK'" class="inline-block">
                        <label v-text="item.sender"></label>
                        <div class="rounded-lg message" v-text="item.message"></div>
                    </div>
                </chat-message>
            </div>
        </v-card>

        <v-card>
            <div class="message-form">
                <v-row class="mt-3" no-gutters>
                    <v-col cols="10" class="mr-4">
                        <v-text-field
                            v-model="message"
                            label="메세지를 입력해주세요"
                            single-line
                            outlined
                            clearable
                            hide-details=""
                            @keyup.enter="sendMessage"
                        ></v-text-field>
                    </v-col>
                    <v-divider></v-divider>
                    <v-col>
                        <v-btn fab depressed @click="sendMessage">전송</v-btn>
                    </v-col>
                </v-row>
            </div>
        </v-card>
    </div>
</template>

<script>
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";
import ChatMessage from "./parts/ChatMessage.vue"

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
            noInputNickname: true
        }
    },
    methods: {
        inputNickname() {
            if (!this.sender) return
            this.noInputNickname = false
            this.joinChat()
        },
        joinChat() {
            websocket = new SockJS(`${location.protocol}//${location.host}/ws/chat`)
            stompClient = Stomp.over(websocket)
            stompClient.connect({}, this.onConnected, this.onError)
        },
        onConnected() {
            stompClient.subscribe(`/sub/chat-room/${this.info.id}`, response => {
                this.messages.push(JSON.parse(response.body))
                /**
                 * Scrollbar end로 조작 시 비동기 데이터를 받고 Dom이 그려진 이후에 동작시켜야 하기 때문에
                 * 아래와 같은 비동기 콜백 함수 안에 실행되도록 해야한다
                 */
                this.$nextTick(() => {
                    this.controlScroll()
                })
            })

            let data = this.getMessageObject("join", "JOIN")
            stompClient.send(`/pub/chat/join`, JSON.stringify(data))
        },
        onError(error) {
            console.log(error)
        },
        sendMessage() {
            if (!this.message) return
            let data = this.getMessageObject(this.message, "TALK")
            stompClient.send(`/pub/chat/message`, JSON.stringify(data))
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
        }
    },
    components: {
        'chat-message': ChatMessage
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
</style>