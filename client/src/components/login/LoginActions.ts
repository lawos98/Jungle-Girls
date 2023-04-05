import api from "../../api/axios";
import Cookies from "js-cookie";

export const login = (email: string, password: string) => {
    api.post('/login', {email, password}).then(response => {
        Cookies.set('token', response.data.token, {httpOnly: true})
    })
}