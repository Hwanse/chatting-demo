<template>
  <v-app>
    
    <div class="app-body">
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
    //
  }),
};
</script>

<style>
.app-body {
  max-width: 650px !important;
  min-width: 500px !important;
  margin-left: auto !important;
  margin-right: auto !important;
}
</style>
