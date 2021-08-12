import request from "../request"

export default {
    async signIn(id, password) {
        try {
            let data = JSON.stringify({userId: id, password: password})
            const response = await request.instance.post('/api/login', data)
            const token = response.data.data.token

            window.localStorage.setItem('authToken', token)
            request.setAuth(token) 
            return token
        } catch(error) {
            console.log(error)
        }
    },
    async signUp(id, password) {
        try {
            let data = JSON.stringify({userId: id, password: password})
            const response = request.post('/api/login', data)
            return response.data.data.token
        } catch(error) {
            console.log(error)
        }
    }
}