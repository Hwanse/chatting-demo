import request from "../request"

export default {
    async signIn(id, password) {
        try {
            let signInData = JSON.stringify({userId: id, password: password})
            const response = await request.post("/api/signin", signInData)
            const token = response.data.data.token
            
            if (token) {
                window.sessionStorage.setItem("authToken", token)
                window.sessionStorage.setItem("authRequire", false)
                request.defaults.headers.common['Authorization'] = token
            }
        } catch(error) {
            console.error(error)
        }
    },
    async signUp(id, password) {
        try {
            let signUpData = JSON.stringify({userId: id, password: password})
            const response = await request.post("/api/signup", signUpData)
            return response.data.data
        } catch(error) {
            console.error(error)
        }
    }
}