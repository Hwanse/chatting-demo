import {shallowMount, createLocalVue} from "@vue/test-utils"
import ChatRoomCreateDialog from "@/views/chat/chatList/parts/ChatRoomCreateDialog.vue"
import Vuetify from "vuetify"

const localVue = createLocalVue()
localVue.use(Vuetify)

describe("test ChatRoomCreateDialog", () => {

    let wrapper = shallowMount(ChatRoomCreateDialog, {
        localVue,
        propsData: {
            show: false
        },
    })
    
    test("is hide when first rendering", () => {
        expect(wrapper.props().show).toBeFalsy()
    })
    
    test("emit @cancel event when click cancel button", async () => {
        wrapper.vm.$emit("@cancel")
        
        await wrapper.findAll("v-btn-stub").at(0).trigger("click")

        expect(wrapper.emitted("@cancel")).toBeTruthy()  
    })

    test("emit @create event when click create button", async () => {
        wrapper.vm.$emit("@create")

        await wrapper.findAll("v-btn-stub").at(1).trigger("click")

        expect(wrapper.emitted("@create")).toBeTruthy()
    })

})

