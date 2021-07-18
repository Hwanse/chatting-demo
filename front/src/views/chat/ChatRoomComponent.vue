<template>
    <div>
        <v-item-group >
            <v-list-item :to="linkTo">
                <v-list-item-content>
                    <v-list-item-title>
                        {{chatRoom.title}}
                    </v-list-item-title>
                    <v-list-item-subtitle>
                        현재 인원 : {{chatRoom.userCount}}
                    </v-list-item-subtitle>
                </v-list-item-content>
            </v-list-item>
        </v-item-group>
        
        <v-divider></v-divider>
    </div>
</template>

<script>
export default {
    props: ['chat'],
    data() {
        return {
            chatRoom: this.chat,
            linkTo: `/chat/${this.chat.id}`
        }
    }
}
</script>