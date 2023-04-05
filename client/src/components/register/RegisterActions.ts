import api from "../../api/axios";
import Cookies from "js-cookie";

export const register = (username: string, firstname: string, lastname: string, password: string) => {
    console.log("Registering user: " + username + " " + firstname + " " + lastname + " " + password)
    api.post('/register', {
            username,
            firstname,
            lastname,
            password
        },
        {
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
        Cookies.set('token', response.data.token, {httpOnly: true})
    })
}