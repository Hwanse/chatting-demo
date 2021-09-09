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
            
            return token
        } catch(error) {
            if (error.response.status === 400) {
                alert("ID 또는 패스워드 정보가 일치하지 않습니다.")
            } else {
                alert("로그인 오류.")
                console.error(error)
            }
        }
    },
    async signUp(id, password) {
        try {
            let signUpData = JSON.stringify({userId: id, password: password})
            const response = await request.post("/api/signup", signUpData)
            return response.data.data
        } catch(error) {
            if (error.response.status === 409) {
                alert("중복된 ID입니다. 다른 ID를 입력해주세요.")
            } else {
                alert("회원가입 오류.")
                console.error(error)
            }
        }
    }
}