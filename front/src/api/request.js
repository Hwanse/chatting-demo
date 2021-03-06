import axios from "axios"

const request = axios.create({
    baseURL: `${process.env.VUE_APP_API_URL}`,
    headers: {
        'Content-Type' : 'application/json'
    },
    timeout: 3000
})

/**
 * 페이지 새로고침 시 axios 인스턴스도 새로 생성되는 것으로 보여
 * 이전에 작업한 로직으로는 API 요청에 Authorization 헤더가 포함되지 않는
 * 문제가 있었다. 따라서 아래와 같이 axios API request 시 인터샙터를 걸어
 * localStorage에 저장된 토큰 값을 꺼내 셋팅하도록 변경 
 */
request.interceptors.request.use(
    async (config) => {
        const token = window.sessionStorage.getItem('authToken')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    error => Promise.reject(error)
)


request.interceptors.response.use(
    response => {
        return response
    }, 
    async (error) => {        
        if (error.response.status === 401) {
            alert("로그인이 만료되었습니다. 로그인을 다시 시도해주세요.")
            window.sessionStorage.setItem("authRequire", true)
            location.href = "/"
        }

        return Promise.reject(error)
    }
)

export default request