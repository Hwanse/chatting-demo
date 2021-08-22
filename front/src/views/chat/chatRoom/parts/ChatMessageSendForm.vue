<template>
    <v-card>
        <div class="message-form">
            <v-row class="mt-3" no-gutters>
                <v-col cols="10" class="mr-4">
                    <v-text-field
                        v-model="message"
                        label="메세지를 입력해주세요"
                        single-line
                        outlined
                        clearable
                        hide-details=""
                        @keyup.enter="sendMessage"
                    ></v-text-field>
                </v-col>
                <v-divider></v-divider>
                <v-col>
                    <v-btn fab depressed v-on:click="sendMessage">전송</v-btn>
                </v-col>
            </v-row>
        </div>
    </v-card>
</template>

<script>
export default {
    data() {
        return {
            message: '',
        }
    },
    methods: {
        sendMessage() {
            if (!this.message.trim()) {
                this.message = ''
                return
            }

            this.$emit("@send", this.message.trim())
            this.message = ''
        }
    },
}
</script>