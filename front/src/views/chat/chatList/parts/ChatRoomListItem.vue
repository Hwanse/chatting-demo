<template>
    <div>
        <v-item-group >
            <v-list-item @click="enterChatRoom">
                <v-list-item-content>
                    <v-list-item-title>
                        {{chatRoom.title}}
                    </v-list-item-title>
                    <v-list-item-subtitle>
                        {{chatRoom.userCount}} / {{chatRoom.limitUserCount}}
                    </v-list-item-subtitle>
                </v-list-item-content>
            </v-list-item>
        </v-item-group>
        
        <v-divider></v-divider>
    </div>
</template>

<script>
import chatApi from "@/api/chat/chatAPI.js"

export default {
    props: ['chat'],
    data() {
        return {
            chatRoom: this.chat,
            linkTo: `/chat/${this.chat.id}`
        }
    }, 
    methods: {
        async enterChatRoom() {
            const room = await chatApi.getChatRoom(this.chat.id)
            if (room) {
                (room.limitUserCount - room.userCount > 0) ? this.goToChatRoom() : this.alertMessage()
            }
        },
        goToChatRoom() {
            this.$router.push({name: "ChatRoomView", params: {id: this.chat.id}})
        },
        alertMessage() {
            alert("현재 채팅방 인원이 가득 찼습니다.")
        }
    }
}
</script>