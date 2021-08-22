import {shallowMount, createLocalVue} from "@vue/test-utils"
import ChatRoomListItem from "@/views/chat/chatList/parts/ChatRoomListItem.vue"
import Vuetify from "vuetify"

const localVue = createLocalVue()
localVue.use(Vuetify)

describe("test ChatRoomListItem Component", () => {

    const chatRoomData = {
        id: 1,
        title: 'title1',
        userCount: 0
    }
    
    let wrapper = shallowMount(ChatRoomListItem, {
        localVue,
        propsData: {
            chat: chatRoomData
        }
    })  
    
    test("has chatRoom title information", () => {
        const title = wrapper.find("v-list-item-title-stub")
        expect(title.text()).toBe(chatRoomData.title)
    })
    
    test("has chatRoom  usercount information", () => {
        const userCount = wrapper.find("v-list-item-subtitle-stub")
        expect(userCount.text()).toBe(`현재 인원 : ${chatRoomData.userCount}`)
    })
    
    test("has chatRoom item linkTo information", () => {
        const item = wrapper.find("v-list-item-stub")
        expect(item.attributes("to")).toBe(`/chat/${chatRoomData.id}`)
    })

})