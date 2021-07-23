<template>
    <div>
        <v-card>
            <div class="list-container">
                <ChatRoomListItem v-for="item in chatList" :key="item.id" :chat="item"/>
            </div>
            
            <ChatCreateButton v-on:@click="showDialog"/>
        </v-card>

        <ChatRoomCreateDialog :show="showCreateDialog" v-on:@create="addChatRoom" v-on:@cancel="hideDialog"/>
    </div>
</template>

<script>
import ChatRoomListItem from "./parts/ChatRoomListItem.vue"
import ChatRoomCreateDialog from "./parts/ChatRoomCreateDialog.vue"
import ChatCreateButton from "./parts/ChatCreateButton.vue"
import axios from "axios"

export default {
    data() {
        return {
            chatList: [],
            showCreateDialog: false,
        }
    },
    mounted() {
        this.getChatList()
            .then(response => {
                this.chatList = response.data
            })
            .catch(reason => console.log(reason))
    },
    components: {
        ChatRoomListItem,
        ChatRoomCreateDialog,
        ChatCreateButton
    },
    methods: {
        async getChatList() {
            return await axios.get(`${location.protocol}//${location.host}/api/chat-room`)
        },
        showDialog() {
            this.showCreateDialog = true
        },
        hideDialog() {
            this.showCreateDialog = false
        },
        addChatRoom(item) {
            this.chatList.unshift(item)
            this.hideDialog()
        }
    }
}
</script>

<style>
.create-button-position {
    position: absolute;
    top: 85%;
    right: 7%;
}
.list-container {
    box-sizing: border-box;
    overflow-y: auto;
    height: calc(100vh - 4rem);
    min-height: 30rem;
}
</style>