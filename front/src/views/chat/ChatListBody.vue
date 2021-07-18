<template>
    <div>
        <v-card>
            <v-virtual-scroll
                :items="chatList"
                item-height="55"
                height="650"
            >
                <template v-slot="{ item }">
                    <chat-room-component :key="item.id" :chat="item"></chat-room-component>
                </template>
            </v-virtual-scroll>
            
            <!-- <div class="fixed-button">
                <v-btn 
                    fab
                    dark
                    color="pink"
                    @click="changeShowForm"
                >
                    <v-icon>mdi-plus</v-icon>
                </v-btn>
            </div> -->

        </v-card>
    </div>
</template>

<script>
import ChatRoomComponent from "./ChatRoomComponent.vue"
// import ChatRoomCreateForm from "./ChatRoomCreateForm.vue"
import axios from "axios"

export default {
    data() {
        return {
            chatList: [],
            showForm: false
        }
    },
    mounted() {
        this.getChatList()
    },
    components: {
        'chat-room-component': ChatRoomComponent,
        // 'chat-room-create-form': ChatRoomCreateForm
    },
    methods: {
        getChatList() {
            axios.get(`${location.protocol}//${location.host}/api/chat-room`)
                .then(response => {
                    this.chatList = response.data
                })
                .catch(error => {
                    console.log(error)
                })
        },
        changeShowForm() {
            this.showForm = !this.showForm
        }
    }

}
</script>

<style>
.fixed-button {
    position: absolute;
    top: 80%;
    right: 10%;
}
</style>