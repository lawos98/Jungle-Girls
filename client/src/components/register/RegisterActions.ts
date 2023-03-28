import api from "../../api/axios";
import {set} from "js-cookie";

export const register = (email: string, studentIndex: string, repositoryLink: string, password: string) => {
    api.post('/register', {email, studentIndex, password}).then(response => {
        set('token', response.data.token, {httpOnly: true})
    })
}