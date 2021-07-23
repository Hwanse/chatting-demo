import {createLocalVue, shallowMount} from "@vue/test-utils"
import axios from "axios"
import ChatListBody from "@/views/chat/chatList/ChatListBody.vue"
import ChatRoomListItem from "@/views/chat/chatList/parts/ChatRoomListItem.vue"
import ChatRoomCreateDialog from "@/views/chat/chatList/parts/ChatRoomCreateDialog.vue"
import ChatCreateButton from "@/views/chat/chatList/parts/ChatCreateButton.vue"
import Vuetify from "vuetify"

const localVue = createLocalVue()
localVue.use(Vuetify)

const chatListApiResponse = {
    data: [
        {id:1, title:"title", userCount:0}
    ]
}

jest.mock("axios")

describe("test ChatListBody Component", () => {

    let wrapper 

    beforeEach(async () => {
        wrapper = shallowMount(ChatListBody, {
            localVue,
            vuetify: new Vuetify(),
            data() {
                return {
                    chatList: []
                }
            },
            stubs: {
                ChatRoomListItem,
                ChatRoomCreateDialog,
                ChatCreateButton
            }
        })

        const data = axios.get.mockResolvedValue(chatListApiResponse)
        
        await data()
            .then(response => {
                wrapper.setData({ chatList : response.data})
            })

    })

    test("has ChatRoomListItem Component", () => {
        const chatRoomListItem = wrapper.findComponent(ChatRoomListItem)
        expect(chatRoomListItem.exists()).toBeTruthy()
    })

    test("has ChatCreateButton Component", () => {
        const chatCreateButton = wrapper.findComponent(ChatCreateButton)
        expect(chatCreateButton.exists()).toBeTruthy()
    })

    test("has ChatRoomCreateDialog Component", () => {
        const chatRoomCreateDialog = wrapper.findComponent(ChatRoomCreateDialog)
        expect(chatRoomCreateDialog.exists()).toBeTruthy()
    })

})