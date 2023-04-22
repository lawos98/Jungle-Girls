import axios,  {AxiosResponse, AxiosError } from "axios";
import Cookies from "js-cookie";

const isPublicRoute = (url: string): boolean => {
    const publicRoutes = ["/register", "/login"];
    return publicRoutes.some(route => url.includes(route));
};

const api = axios.create({
    baseURL: "http://localhost:8080/api",
});

api.interceptors.request.use((config) => {
    if (!isPublicRoute(config.url || "")) {
        config.headers.Authorization = `Bearer ${Cookies.get("token")}`;
    }
    return config;
}, (error: AxiosError) => {
    console.log(error);
    return Promise.reject(error);
});

api.interceptors.response.use(
    (response: AxiosResponse) => response,
    (error: AxiosError) => {
        console.log(error);
        return Promise.reject(error);
    }
);

export default api;
