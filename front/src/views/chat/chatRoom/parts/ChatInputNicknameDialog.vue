<template>
    <v-dialog v-model="isInputNickname" persistent max-width="500">
        <v-card class="pa-3">
            <v-row no-gutters>
                <v-text-field
                    v-model="nickname"
                    label="사용할 닉네임을 입력해주세요"
                    single-line
                    outlined
                    clearable
                    hide-details=""
                    class="mr-3"
                ></v-text-field>
                <v-btn x-large color="primary" v-on:click="inputNickname">입장</v-btn>
            </v-row>
        </v-card>
    </v-dialog>
</template>

<script>
export default {
    props: ["isInputNickname"],
    data() {
        return {
            nickname: '',
        }
    },
    watch: {
        isInputNickname(newVal) {
            this.isInputNickname = newVal
        }
    },
    methods: {
        inputNickname() {
            if (!this.nickname.trim()) {
                this.nickname = ''
                return
            }
            
            this.$emit("@click", this.nickname.trim())
        }
    }
}
</script>