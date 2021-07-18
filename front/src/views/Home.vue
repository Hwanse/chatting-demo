<template>
  <hello-world />
</template>

<script>
import HelloWorld from "../components/HelloWorld"
import request from "request"

export default {
  name: "Home",
  components: {
    HelloWorld,
  },
  mounted() {
    request(`${location.protocol}//${location.host}/api/hello`, function (error, response, body) {
      window.console.log(`error : ${error}`)
      window.console.log(`statusCode : ${response && response.statusCode}`)
      window.console.log(`body : ${body}`)
    })
  }
};
</script>
