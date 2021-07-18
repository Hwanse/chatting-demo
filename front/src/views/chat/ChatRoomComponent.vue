<template>
    <div>
        <v-item-group >
            <v-list-item>
                <v-list-item-content>
                    <v-list-item-title>
                        {{chatRoom.title}}
                    </v-list-item-title>
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
            chatRoom: this.chat
        }
    }
}
</script>