<template>
    <div>
        <ChatRoomHead :info="room"></ChatRoomHead>
        <ChatRoomBody :info="room" @reload="updateUserCount"></ChatRoomBody>
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
            this.room.userCount = userCount
        }
    },
    components: {
        ChatRoomHead,
        ChatRoomBody
    }
}
</script>