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
            mySessionId: null
        }
    },
    created() {
        this.bus.$on("connected", () => {
            this.sendJoinMessage() 
        })
    },
    methods: {
        inputNickname(nickname) {
            if (!nickname.trim()) return

            this.noInputNickname = false
            this.nickname = nickname

            this.websocket = new SockJS(`${location.protocol}//${location.host}/ws/chat`)
            // let options = {debug: false, protocols: Stomp.VERSIONS.supportedProtocols()};
            this.stompClient = Stomp.over(this.websocket)
            
            this.stompClient.connect({}, this.onConnected, this.onConnectError)
        },
        async onConnected() {
            await this.bindSessionId()

            this.stompClient.subscribe(`/sub/chat-room/${this.info.id}`, this.receiveJoinMessage)
            this.stompClient.subscribe(`/user/${this.mySessionId}/sub/chat-room/${this.info.id}/voice`, this.receiveSignallingMessage)
            this.stompClient.subscribe(`/sub/chat-room/${this.info.id}/leave`, this.receiveLeaveMessage)
            window.addEventListener("beforeunload", this.closeEvent)
            
            this.bus.$emit("connect", this.chatSetupData())           
        },
        onConnectError(error) {
            console.log("stomp connect error : ", error)
        },
        async bindSessionId() {
            let regex = /\/ws\/chat\/[0-9]+\/(\w+)\.*/g
            let result = regex.exec(this.websocket._transport.url)
            if (result) {
                this.mySessionId = result[1] ? result[1] : null
                await this.$nextTick()
            }
        },
        chatSetupData() {
            return {
                stompClient: this.stompClient,
                mySessionId: this.mySessionId,
                roomId: this.info.id,
                sender: this.nickname
            }
        },
        sendJoinMessage() {
            let data = {
                roomId: this.info.id,
                message: "JOIN",
                sender: this.nickname,
                messageType: "JOIN",
                sessionId: this.mySessionId
            } 
            this.stompClient.send("/pub/chat/join", JSON.stringify(data))
        },
        receiveJoinMessage(response) {
            this.bus.$emit("join", response)
        },
        receiveSignallingMessage(response) {
            this.bus.$emit("signalling", response)
        },
        receiveLeaveMessage(response) {
            this.bus.$emit("leave", response)
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