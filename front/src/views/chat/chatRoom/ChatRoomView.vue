<template>
    <div>
        <chat-room-head :info="room"></chat-room-head>
        <chat-room-body :info="room"></chat-room-body>
    </div>
</template>

<script>
import ChatRoomHead from "./ChatRoomHead.vue"
import ChatRoomBody from "./ChatRoomBody.vue"
import axios from "axios";

export default {
    data() {
        return {
            room: {}
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
    },
    components: {
        'chat-room-head': ChatRoomHead,
        'chat-room-body': ChatRoomBody
    }
}
</script>