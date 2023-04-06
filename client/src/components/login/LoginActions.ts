import api from "../../api/axios";
import Cookies from "js-cookie";

export const login = (email: string, password: string) => {
    let payload = {
        username: email, // tymczasowy fix, bo backend wymaga username, a nie email
        password: password
    }
    api.post('/login', payload).then(response => {
        Cookies.set('token', response.data.token, {httpOnly: true})
        console.log("response.data.token: " + response.data.token)
    }).catch(error => {
        console.error(error);
    });
}