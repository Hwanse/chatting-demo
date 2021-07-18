<template>
    <v-container class="pa-0">
        <chat-list-head></chat-list-head>
        <chat-list-body></chat-list-body>
    </v-container>
</template>

<script>
import ChatListHead from "./ChatListHead.vue"
import ChatListBody from "./ChatListBody.vue"

export default {
    data() {
        return {}
    },
    components: {
        'chat-list-head': ChatListHead,
        'chat-list-body': ChatListBody
    }
}
</script>