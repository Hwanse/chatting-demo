<template>
    
    <v-card v-show="form">
        <div class="mt-1">
            <v-card-text>
                <v-text-field v-model="title" label="채팅방 제목을 입력해주세요" single-line ></v-text-field>
                <small class="grey--text">* 채팅방을 새로 개설합니다.</small>
            </v-card-text>
        </div>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn text color="primary" @create="chatRoomCreate">만들기</v-btn>
        </v-card-actions>
    </v-card>
    
</template>

<script>
export default {
    props: ['show'],
    data() {
        return {
            title: '',
            form: this.show
        }
    },
    watch: {
        show(newVal) {
            this.form = newVal
        }
    },
    methods: {
        chatRoomCreate() {
            this.form = false
            this.$emit("@create")
        }
    }
}
</script>