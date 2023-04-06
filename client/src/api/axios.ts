import axios from 'axios';
import Cookies from 'js-cookie';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
});

api.interceptors.request.use(config => {
    const token = Cookies.get('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    response => response,
    error => {
        console.log(error);
        if (error.response && error.response.status === 401) {
            // handle unauthorized error
            // for example, redirect to login page or show error message
            console.log("error.response.data.message: " + error.response.data.message);
        }
        return Promise.reject(error);
    }
);

export default api;
