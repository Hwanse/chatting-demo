<template>
    <div>
        <v-form ref="loginForm">
            <v-text-field v-model="id" label="ID" required></v-text-field>
        
            <v-text-field v-model="password" label="Password" type="password" required></v-text-field>
        </v-form>
        <v-btn color="success" class="mr-4" @click="signIn">
            Sign-In
        </v-btn>
        <v-btn color="info" class="mr-4" @click="showSignUpForm">
            Sign-Up
        </v-btn>

        <SignUpFormDialog :signUpForm="signUpForm" v-on:@cancel="hideSignUpForm"></SignUpFormDialog>
    </div>
</template>

<script>
import signApi from "@/api/sign/signAPI.js"
import SignUpFormDialog from "./SignUpFormDialog.vue"

export default {
    data() {
        return {
            id: '',
            password: '',
            signUpForm: false
        }
    },
    methods: {
        async signIn() {
            await signApi.signIn(this.id, this.password)
            this.goChatListView()
        },
        showSignUpForm() {
            this.signUpForm = true
        },
        hideSignUpForm() {
            this.signUpForm = false
        },
        goChatListView() {
            this.$router.push({name: 'ChatListView'})
        }
    },
    components: {
        SignUpFormDialog
    }
}
</script>