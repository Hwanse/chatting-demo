<template>
    <div class="chat-room-container">
        <ChatRoomHead :info="room"/>
        <ChatRoomBody :info="room" v-on:monitoring="updateUserCount"></ChatRoomBody>
    </div>
</template>

<script>
import ChatRoomHead from "./ChatRoomHead.vue"
import ChatRoomBody from "./ChatRoomBody.vue"
import chatApi from "@/api/chat/chatAPI.js"

export default {
    props: ['id'],
    data() {
        return {
            room: {},
            pollingRoomData: null,
        }
    },
    created() {
        this.bindChatRoomData()
    },
    methods: {
        async bindChatRoomData() {
            const chatRoomInfo = await chatApi.getChatRoom(this.id)
            this.room = chatRoomInfo
        },
        updateUserCount(userCount) {
            this.room.userCount = userCount
        }
    },
    components: {
        ChatRoomHead,
        ChatRoomBody
    }
}
</script>