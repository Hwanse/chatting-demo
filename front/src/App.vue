<template>
  <v-app >
      <div class="app">
        <v-main>
          <router-view />
        </v-main>
      </div>
  </v-app>
</template>

<script>
export default {
  name: "App",
  data: () => ({
  }),
  mounted() {
    document.title = "chat-front"
  }
};
</script>

<style>
html, body {
  display: flex;
  justify-content: center;
}
.app {
  width: 100vmin;
  height: 100vmin;
}
</style>
