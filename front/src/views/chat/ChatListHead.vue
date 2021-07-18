<template>
    <v-app-bar color="primary" flat>
        <v-col cols="10">
            <v-text-field
                label="검색"
                prepend-icon="mdi-magnify"
                single-line
                dark
            ></v-text-field>
        </v-col>
        <v-spacer></v-spacer>
    </v-app-bar>
</template>

<script>
export default {

}
</script>

<style>
.v-toolbar__content {
    height: 80px !important;
}
</style>