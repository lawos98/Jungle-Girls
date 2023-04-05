import axios from 'axios';
import Cookies from "js-cookie";


const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        Authorization: `Bearer ${Cookies.get('token')}`,
    },
});

api.interceptors.response.use(
    response => response,
    error => {
        console.log(error);
        return Promise.reject(error);
    }
);

export default api;