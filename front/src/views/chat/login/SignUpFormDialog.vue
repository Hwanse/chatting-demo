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
import signApi from "@/api/sign/signAPI.js"

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
        async signUp() {
            const userInfo = await signApi.signUp(this.id, this.password)
            if (!userInfo) {
                alert("회원가입 오류")
            }
            this.cancel()
        }
    }
}
</script>