<template>
    <div>
        <ChatInputNicknameDialog :isInputNickname="noInputNickname" v-on:@click="inputNickname"/>

        <v-card>
            <ChatTabs :roomId="info.id" :inputNickname="nickname" :bus="bus" :stompClient="stompClient"/>
        </v-card>
    </div>
</template>

<script>
import Vue from "vue"
import SockJS from "sockjs-client"
import Stomp from "webstomp-client"
import ChatInputNicknameDialog from "./parts/ChatInputNicknameDialog.vue"
import ChatTabs from "./parts/ChatTabs.vue"

export default {
    props: ['info'],
    data() {
        return {
            bus: new Vue(),
            websocket: null,
            stompClient: null,
            nickname: '',
            noInputNickname: true,
        }
    },
    methods: {
        async inputNickname(nickname) {
            if (!nickname.trim()) return

            this.noInputNickname = false
            this.nickname = nickname

            this.websocket = new SockJS(`${location.protocol}//${location.host}/ws/chat`)
            // let options = {debug: false, protocols: Stomp.VERSIONS.supportedProtocols()};
            this.stompClient = Stomp.over(this.websocket)
            
            await this.stompClient.connect({}, this.onConnected, this.onConnectError)
        },
        onConnected() {
            this.bindSessionId()
            this.stompClient.subscribe(`/sub/chat-room/${this.info.id}`, response => this.bus.$emit("join", response))
            this.stompClient.subscribe(`/user/${this.bus.$data.mySessionId}/sub/chat-room/${this.info.id}/voice`, response => this.bus.$emit("signalling", response))
            this.stompClient.subscribe(`/sub/chat-room/${this.info.id}/leave`, response => this.bus.$emit("leave", response))
            window.addEventListener("beforeunload", this.closeEvent)

            let data = {
                roomId: this.info.id,
                message: "JOIN",
                sender: this.nickname,
                messageType: "JOIN"
            } 
            this.stompClient.send("/pub/chat/join", JSON.stringify(data))
        },
        onConnectError(error) {
            console.log("stomp connect error : ", error)
        },
        bindSessionId() {
            let regex = /\/ws\/chat\/[0-9]+\/(\w+)\.*/g
            let result = regex.exec(this.websocket._transport.url)
            if (result) {
                this.bus.$data.mySessionId = result[1] ? result[1] : null
            }
        },
        closeEvent(event) {
            event.preventDefault()
            this.stompClient.disconnect()
            this.websocket.close()
        } 
    },
    components: {
        ChatInputNicknameDialog,
        ChatTabs
    }
}
</script>