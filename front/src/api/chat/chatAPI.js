import request from "../request"

export default {
    async getChatList() {
        try {
            const response = await request.instance.get('/api/chat-room')
            return response.data.data
        } catch(error) {
            console.log(error)
        }
    },
    async createChatRoom(title) {
        try {
            let chatRoomData = JSON.stringify({title: title})
            const response = await request.instance.post('/api/chat-room', chatRoomData)
            return response.data.data
        } catch(error) {
            console.log(error)
        }
    },
    async getChatRoom(roomId) {
        try {
            const response = await request.instance.get(`/api/chat-room/${roomId}`)
            return response.data.data
        } catch(error) {
            console.log(error)
        }
    }
}