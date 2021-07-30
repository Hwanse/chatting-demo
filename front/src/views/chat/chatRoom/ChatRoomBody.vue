<template>
    <div>
        <ChatInputNicknameDialog :isInputNickname="noInputNickname" v-on:@click="inputNickname"/>

        <v-card>
            <ChatTabs :roomId="info.id" :inputNickname="nickname" />
        </v-card>
    </div>
</template>

<script>
import ChatInputNicknameDialog from "./parts/ChatInputNicknameDialog.vue"
import ChatTabs from "./parts/ChatTabs.vue"


export default {
    props: ['info'],
    data() {
        return {
            nickname: '',
            noInputNickname: true,
        }
    },
    methods: {
        inputNickname(nickname) {
            if (!nickname.trim()) return

            this.noInputNickname = false
            this.nickname = nickname
        },
    },
    components: {
        ChatInputNicknameDialog,
        ChatTabs
    }
}
</script>