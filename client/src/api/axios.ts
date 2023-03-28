import axios from 'axios';
import {get} from 'js-cookie';


const api = axios.create({
    baseURL: 'http://localhost:3333',
    headers: {
        Authorization: `Bearer ${get('token')}`
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