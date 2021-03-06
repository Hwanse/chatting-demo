const path = require("path")

module.exports = {
  devServer: {
    // disableHostCheck: true,
    // public: "0.0.0.0:3000",
    // port: 8080,
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

  configureWebpack: {
    resolve: {
      alias: {
        "@": path.join(__dirname, "src/")
      }
    },
  },

  transpileDependencies: ["vuetify"],
};
