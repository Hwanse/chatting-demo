<template>
    <v-container class="pa-0">
        <ChatListHead/>
        <ChatListBody/>
    </v-container>
</template>

<script>
import ChatListHead from "./ChatListHead.vue"
import ChatListBody from "./ChatListBody.vue"

export default {
    components: {
        ChatListHead,
        ChatListBody
    }
}
</script>