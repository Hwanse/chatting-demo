<template>
    <v-dialog v-model="show" persistent max-width="500">
        <v-card class="pa-4">

            <v-text-field
                v-model="title"
                label="생성할 채팅방 제목을 입력해주세요"
                single-line
                outlined
                clearable
                hide-details=""
                class="mb-5"
            ></v-text-field>

            <v-row>
                <v-spacer></v-spacer>
                <div class="mr-3">
                    <v-btn text color="pink" class="mr-4" @click="cancel">취소</v-btn>
                    <v-btn text color="primary" @click="create">만들기</v-btn>
                </div>
            </v-row>

        </v-card>
    </v-dialog>
</template>

<script>
import axios from "axios"

export default {
    props: ["show"],
    data() {
        return {
            title: ''
        }
    },
    methods: {
        cancel() {
            this.title = ''
            this.$emit("@cancel")
        },
        create() {
            if (!this.title.trim()) return

            axios.post(`${location.protocol}//${location.host}/api/chat-room`, 
                        JSON.stringify({title : this.title}),
                        { headers : {"Content-Type" : "application/json"}})
                 .then(response => {
                    this.title = ''
                    this.$emit("@create", response.data)
                 })
                 .catch(reason => console.log(reason))
        }
    },
}
</script>