import {shallowMount, createLocalVue} from "@vue/test-utils"
import ChatCreateButton from "@/views/chat/chatList/parts/ChatCreateButton.vue"
import Vuetify from "vuetify"

const localVue = createLocalVue()
localVue.use(Vuetify)

describe("test ChatCreateButton Component", () => {
    
    test("is Button and Icon", () => {
        const wrapper = shallowMount(ChatCreateButton, {
            localVue
        })
    
        expect(wrapper.find("v-btn").exists()).toBeTruthy
        expect(wrapper.find("v-icon").exists()).toBeTruthy
    })
    
})