<template>
    <div>
        <v-dialog v-model="noInputNickname" persistent>
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
            <div class="chat-container">
                <chat-message v-for="(item, index) in messages" :key="index" :item="item">
                    <span v-if="item.messageType === 'JOIN'" class="inline-block">
                        {{item.sender}}님이 입장하셨습니다.
                    </span>
                    <div v-if="item.messageType === 'TALK'" class="inline-block">
                        <label v-text="item.sender"></label>
                        <div class="rounded-lg message" v-text="item.message"></div>
                    </div>
                    <span v-if="item.messageType === 'LEAVE'" class="inline-block">
                        {{item.sender}}님이 퇴장하셨습니다.
                    </span>
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
import axios from "axios";
import ChatMessage from "./parts/ChatMessage.vue"

let websocket
let stompClient

export default {
    data() {
        return {
            message: '',
            room: {},
            messages: [],
            sender: '',
            noInputNickname: true
        }
    },
    created() {
        this.findRoom()
    },
    methods: {
        findRoom() {
            let roomId = this.$route.params.id
            axios.get(`${location.protocol}//${location.host}/api/chat-room/${roomId}`)
                .then(response => {
                    this.room = response.data
                })
                .catch(error => {
                    console.log(error)
                })
        },
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
            stompClient.subscribe(`/sub/chat-room/${this.room.id}`, response => {
                this.messages.push(JSON.parse(response.body)) 
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
            this.message = ''
            stompClient.send(`/pub/chat/message`, JSON.stringify(data))
        },
        getMessageObject(content, type) {
            return {
                roomId: this.room.id,
                message: content,
                sender: this.sender,
                messageType: type
            }
        },
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
    padding: 1rem;
    height: calc(100vh - 9.5rem);
    min-height: 30rem;
}
.message-form {
    padding: 10px;
    min-height: 7.5rem;
}
</style>