<template>
    <div>
        <v-dialog v-model="signUpForm" persistent max-width="500">
            <v-card class="pa-3">
                <v-text-field v-model="id" label="ID" required></v-text-field>
                <v-text-field v-model="password" label="Password" required></v-text-field>

                <v-btn x-large color="cancel" class="mr-3" @click="cancel">Cancel</v-btn>
                <v-btn x-large color="primary" @click="signUp">Submit</v-btn>
            </v-card>
        </v-dialog>
    </div>
</template>

<script>
import axios from "axios"

export default {
    props: ['signUpForm'],
    watch: {
        signUpForm(newVal) {
            this.signUpForm = newVal
        }
    },
    data() {
        return {
            id: '',
            password: ''
        }
    },
    methods: {
        cancel() {
            this.$emit("@cancel")
        },
        signUp() {
            let data = {
                userId: this.id,
                password: this.password
            }

            axios.post(`${location.protocol}//${location.host}/api/signup`, 
                       JSON.stringify(data), 
                       {
                          headers: { 'Content-Type' : 'application/json'}
                       })
                .then(() => this.cancel())
        }
    }
}
</script>