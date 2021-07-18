<template>
    <div>
        <chat-room-head></chat-room-head>
        <chat-room-body></chat-room-body>
        <h2>채팅방</h2>
    </div>
</template>

<script>
import ChatRoomHead from "./ChatRoomHead.vue"
import ChatRoomBody from "./ChatRoomBody.vue"

export default {
    components: {
        'chat-room-head': ChatRoomHead,
        'chat-room-body': ChatRoomBody
    }
}
</script>