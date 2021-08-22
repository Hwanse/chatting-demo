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
import chatApi from "@/api/chat/chatAPI.js"

export default {
    data() {
        return {
            chatList: [],
            showCreateDialog: false,
        }
    },
    mounted() {
        this.bindChatList()
    },
    components: {
        ChatRoomListItem,
        ChatRoomCreateDialog,
        ChatCreateButton
    },
    methods: {
        async bindChatList() {
            const chatList = await chatApi.getChatList()
            this.chatList = chatList
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
    top: 80%;
    right: 7%;
}
.list-container {
    box-sizing: border-box;
    overflow-y: auto;
    height: 92vh;
}
</style>