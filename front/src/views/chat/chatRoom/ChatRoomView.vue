<template>
    <div>
        <chat-room-head :info="room"></chat-room-head>
        <chat-room-body :info="room" @reload="updateUserCount"></chat-room-body>
    </div>
</template>

<script>
import ChatRoomHead from "./ChatRoomHead.vue"
import ChatRoomBody from "./ChatRoomBody.vue"
import axios from "axios";

export default {
    props: ['id'],
    data() {
        return {
            room: {},
            pollingRoomData: null,
        }
    },
    created() {
        this.findRoom()
    },
    methods: {
        findRoom() {
            axios.get(`${location.protocol}//${location.host}/api/chat-room/${this.id}`)
                .then(response => {
                    this.room = response.data
                })
                .catch(error => {
                    console.log(error)
                })
        },
        updateUserCount(userCount) {
            console.log(`update event : ${userCount}`)
            this.room.userCount = userCount
        }
    },
    components: {
        'chat-room-head': ChatRoomHead,
        'chat-room-body': ChatRoomBody
    }
}
</script>