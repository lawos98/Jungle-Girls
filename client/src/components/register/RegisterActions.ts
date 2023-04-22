import api from "../../api/axios";
import Cookies from "js-cookie";

export const register = (username: string, firstname: string, lastname: string, password: string, successCallback:Function, errorCallback: Function) => {
    const payload = {
        username : username,
        firstname : firstname,
        lastname : lastname,
        password : password
    };
    api.post("/register", payload,
        {
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => {
        Cookies.set("token", response.data.token);
        successCallback(response.data);
    }).catch(error => {
        if (error.response) {
            errorCallback(error.response.data.message);
        }
    });
};