const path = require("path")

module.exports = {
//   outputDir: path.resolve(__dirname, `../server/src/main/resources/static`),
  devServer: {
    proxy: {
      "/api": {
        target: "http://localhost:8081",
        changeOrigin: true // cors 문제를 막기위한 옵션
      },
      "/ws/chat": {
        target: "http://localhost:8081",
        changeOrigin: true,
        ws: true // proxy websocket
      }
    },
  },

  transpileDependencies: ["vuetify"],
};
