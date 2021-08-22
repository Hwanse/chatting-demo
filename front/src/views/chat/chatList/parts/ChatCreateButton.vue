<template>
    <v-btn
        color="pink"
        fab
        dark
        class="create-button-position"
        @click="showDialog"
    >
        <v-icon>mdi-plus</v-icon>
    </v-btn>
</template>

<script>
export default {
    methods: {
        showDialog() {
            this.$emit("@click")
        }
    },
}
</script>