import axios from "axios"

const request = {
    instance: axios.create({
        baseURL: `${location.protocol}//${location.host}`,
        headers: {
            'Content-Type' : 'application/json'
        },
        timeout: 3000
    }),
    setAuth(token) {
        const tokenType = 'Bearer'
        this.instance.defaults.headers.common['Authorization'] = `${tokenType} ${token}`
    }
}

export default request