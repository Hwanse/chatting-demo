<template>
    <div>
        <v-form ref="loginForm">
            <v-text-field v-model="id" label="ID" required></v-text-field>
        
            <v-text-field v-model="password" label="Password" required></v-text-field>
        </v-form>
        <v-btn color="success" class="mr-4" @click="login">
            Sign-In
        </v-btn>
        <v-btn color="info" class="mr-4" @click="showSignUpForm">
            Sign-Up
        </v-btn>

        <SignUpFormDialog :signUpForm="signUpForm" v-on:@cancel="hideSignUpForm"></SignUpFormDialog>
    </div>
</template>

<script>
import axios from "axios"
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
        async login() {
            let data = {
                userId: this.id,
                password: this.password
            }
            axios.post(`${location.protocol}//${location.host}/api/login`, 
                       JSON.stringify(data), 
                       {
                          headers: { 'Content-Type' : 'application/json'}
                       })
                .then(response => {
                    const token = response.data.data.token
                    console.log(token)
                })
        },
        showSignUpForm() {
            this.signUpForm = true
        },
        hideSignUpForm() {
            this.signUpForm = false
        }
    },
    components: {
        SignUpFormDialog
    }
}
</script>