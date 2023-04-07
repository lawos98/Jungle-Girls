import api from "../../api/axios";
import Cookies from "js-cookie";

export const register = (username: string, firstname: string, lastname: string, password: string) => {
    console.log("Registering user: " + username + " " + firstname + " " + lastname + " " + password)
    let payload = {
        username : "test213",
        firstname : "Test",
        lastname : "Test",
        password : "Testeeee$2"
    }

    api.post('/register', payload,{headers:{"Content-Type": "application/json"}}).then(response => {
        Cookies.set('token', response.data.token, {httpOnly: true})
        console.log("response.data.token: " + response.data.token)
    }).catch(error => {
        console.error(error);
    });
}