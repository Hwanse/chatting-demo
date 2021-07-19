<template>
    <div>
        <v-card>
            <div class="chat-container">
                <div v-for="(item, index) in messages" :key="index"> 
                    {{item.message}}
                </div>
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

let websocket
let stompClient

export default {
    data() {
        return {
            message: '',
            room: {},
            messages: [],
        }
    },
    created() {
        this.findRoom()
        this.setup()
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
        setup() {
            websocket = new SockJS(`${location.protocol}//${location.host}/ws/chat`)
            stompClient = Stomp.over(websocket)
            stompClient.connect({}, this.onConnected, this.onError)
        },
        onConnected() {
            stompClient.send(`/pub/chat/enter`)

            stompClient.subscribe(`/sub/chat-room/${this.room.id}`, response => {
                this.messages.push(JSON.parse(response.body)) 
            })
        },
        onError(error) {
            console.log(error)
        },
        sendMessage() {
            let data = {
                roomId: this.room.id,
                message: this.message,
                sender: 'user'
            }
            this.message = ''
            stompClient.send(`/pub/chat/message`, JSON.stringify(data))
        },
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