import request from "../request"

export default {
    async signIn(id, password) {
        try {
            let signInData = JSON.stringify({userId: id, password: password})
            const response = await request.post('/api/login', signInData)
            const token = response.data.data.token
            
            if (token) {
                window.localStorage.setItem('authToken', token)
            }
        } catch(error) {
            console.log(error)
        }
    },
    async signUp(id, password) {
        try {
            let signUpData = JSON.stringify({userId: id, password: password})
            const response = await request.post('/api/signup', signUpData)
            return response.data.data
        } catch(error) {
            console.log(error)
        }
    }
}