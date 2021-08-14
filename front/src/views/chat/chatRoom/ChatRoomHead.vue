<template>
    <v-toolbar dark color="primary" flat>
        <v-row class="d-flex align-center">
            <v-btn icon @click="goToChatList" title="채팅방 나가기">
                <v-icon large>mdi-chevron-left</v-icon>
            </v-btn>
            <v-toolbar-title>{{info.title}}</v-toolbar-title>
            <v-spacer></v-spacer>
            
            <span class="mr-4">
                <v-icon class="mr-1">mdi-account-multiple</v-icon>
                <span v-text="info.userCount" >{{userCount}}</span>
            </span>
            
            <span v-if="info.meManager" title="채팅방 종료" @click="closeChatRoom"> 
                <v-icon class="mr-1">mdi-close-outline</v-icon>
            </span>

        </v-row>
    </v-toolbar>
</template>

<script>
import chatApi from "@/api/chat/chatAPI.js" 

export default {
    props: ['info'],
    data() {
        return {
            userCount: 0
        }
    },
    watch: {
        info(newVal) {
            this.userCount = newVal
        }
    },
    methods: {
        goToChatList() {
            this.$router.push({name: "ChatListView"})
        },
        closeChatRoom() {
            if (!alert("채팅방을 종료하시겠습니까?")) {
                const result = chatApi.disableChatRoom(this.info.id)
                
                if (result) alert("채팅방이 종료되었습니다.")
            }
        }
    }
}
</script>