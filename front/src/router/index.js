import Vue from "vue"
import VueRouter from "vue-router"

import SignInForm from "@/views/chat/login/SignInForm.vue"
import ChatListView from "@/views/chat/chatList/ChatListView.vue"
import ChatRoomView from "@/views/chat/chatRoom/ChatRoomView.vue"

Vue.use(VueRouter);

const requireAuth = () => (to, from, next) => {
    const token = window.sessionStorage.getItem("authToken")
    const authRequire = !window.sessionStorage.getItem("authRequire") ? true : window.sessionStorage.getItem("authRequire")
    
    if (!token && authRequire) {
      alert("로그인이 필요합니다.")
      next("/")
    } else {
      next()
    }
}

const routes = [
    {
      path: "/",
      name: "SignInForm",
      component: SignInForm
    },
    {
      path: "/chat-list",
      name: "ChatListView",
      component: ChatListView,
      beforeEnter: requireAuth()
    },
    {
      path: "/chat/:id",
      name: "ChatRoomView",
      component: ChatRoomView,
      props: true,
      beforeEnter: requireAuth()
    }
]

const router = new VueRouter({
    mode: "history",
    base: process.env.BASE_URL,
    routes
})

export default router;
