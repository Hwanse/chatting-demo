import request from "../request"

export default {
    async getChatList() {
        try {
            const response = await request.get("/api/chat-room")
            return response.data.data.content
        } catch(error) {
            console.error(error)
        }
    },
    async createChatRoom(title) {
        try {
            let chatRoomData = JSON.stringify({title: title})
            const response = await request.post('/api/chat-room', chatRoomData)
            return response.data.data
        } catch(error) {
            console.error(error)
        }
    },
    async getChatRoom(roomId) {
        try {
            const response = await request.get(`/api/chat-room/${roomId}`)
            return response.data.data
        } catch(error) {
            console.error(error)
        }
    },
    async disableChatRoom(roomId) {
        try {
            const response = await request.patch(`/api/chat-room/${roomId}`)
            return response.success
        } catch (error) {
            console.error(error)
        }
    }
}