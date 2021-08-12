<template>
    <v-toolbar dark color="primary" flat>
        <v-row class="d-flex align-center">
            <v-btn icon @click="goToChatList">
                <v-icon large>mdi-chevron-left</v-icon>
            </v-btn>
            <v-toolbar-title>{{info.title}}</v-toolbar-title>
            <v-spacer></v-spacer>
            <v-icon class="mr-1">mdi-account-multiple</v-icon>
            <span v-text="info.userCount" class="mr-2">{{userCount}}</span>
        </v-row>
    </v-toolbar>
</template>

<script>
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
        }
    }
}
</script>