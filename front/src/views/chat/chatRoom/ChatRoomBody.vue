<template>
    <div>
        <ChatInputNicknameDialog :isInputNickname="noInputNickname" v-on:@click="inputNickname"/>

        <v-card>
            <ChatTabs :bus="bus"/>
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
            mySessionId: null,
            monitoringInterval: null,
        }
    },
    created() {
        this.bus.$on("connected", () => {
            this.sendJoinMessage() 
            this.sendMonitoringMessage()
        })
    },
    beforeDestroy() {
        this.bus.$emit("disconnect")
        this.closeEvent()
    },
    methods: {
        inputNickname(nickname) {
            if (!nickname.trim()) return

            this.noInputNickname = false
            this.nickname = nickname
            const token = window.sessionStorage.getItem("authToken")

            // 웹 소켓 스펙 특성상 SockJs API를 활용한 헤더를 조작하여 토큰을 넣어줄수 없다. 따라서 쿼리스트링으로 전달 
            this.websocket = new SockJS(`${location.protocol}//${location.host}/ws/chat?token=${token}`)
            // let options = {debug: false, protocols: Stomp.VERSIONS.supportedProtocols()};
            this.stompClient = Stomp.over(this.websocket)
            
            this.stompClient.connect({"Authorization": `Bearer ${token}`}, this.onConnected, this.onConnectError)
        },
        async onConnected() {
            await this.bindSessionId()

            this.stompClient.subscribe(`/sub/chat-room/${this.info.id}`, this.receiveJoinMessage)
            this.stompClient.subscribe(`/user/${this.mySessionId}/sub/chat-room/${this.info.id}/voice`, this.receiveSignallingMessage)
            this.stompClient.subscribe(`/user/${this.mySessionId}/sub/chat-room/${this.info.id}/monitoring`, this.receiveMonitoringMessage)
            this.stompClient.subscribe(`/sub/chat-room/${this.info.id}/leave`, this.receiveLeaveMessage)
            
            this.bus.$emit("connect", this.chatSetupData())           
            
            window.addEventListener("beforeunload", this.closeEvent)
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
        sendMonitoringMessage() {
            this.monitoringInterval = setInterval(() => {
                let data = {
                    roomId: this.info.id
                }
                this.stompClient.send("/pub/chat/monitoring", JSON.stringify(data))
            }, 2000)
        },
        receiveJoinMessage(response) {
            this.bus.$emit("join", response)
        },
        receiveMonitoringMessage(response) {
            let userCount = JSON.parse(response.body).userCount
            this.$emit("monitoring", userCount)
        },
        receiveSignallingMessage(response) {
            this.bus.$emit("signalling", response)
        },
        receiveLeaveMessage(response) {
            this.bus.$emit("leave", response)
        },
        closeEvent(event) {
            if (event) event.preventDefault()
            clearInterval(this.monitoringInterval)
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