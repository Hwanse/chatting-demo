<template>
    <div>
        <v-card>
            <v-virtual-scroll
                :items="chatList"
                item-height="70"
                height="650"
            >
                <template v-slot="{ item }">
                    <chat-room-component :key="item.id" :chat="item"></chat-room-component>
                </template>
            </v-virtual-scroll>
        </v-card>
    </div>
</template>

<script>
import ChatRoomComponent from "./ChatRoomComponent.vue"
import axios from "axios"

export default {
    data() {
        return {
            chatList: []
        }
    },
    mounted() {
        this.getChatList()
    },
    components: {
        'chat-room-component': ChatRoomComponent
    },
    methods: {
        getChatList() {
            axios.get(`${location.protocol}//${location.host}/api/chat-room`)
                .then(response => {
                    console.log(response.data)
                    this.chatList = response.data
                })
                .catch(error => {
                    console.log(error)
                })
        }
    }

}
</script>