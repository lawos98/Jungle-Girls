import api from "../../api/axios";
import {set} from "js-cookie";

export const login = (email: string, password: string) => {
    api.post('/login', {email, password}).then(response => {
        set('token', response.data.token, {httpOnly: true})
    })
}