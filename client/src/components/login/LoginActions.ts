import api from "../../api/axios";
import Cookies from "js-cookie";

export const login = (username: string, password: string) => {
    const payload = {
        username : username,
        password : password
    };
    api.post("/login", payload,
        {
            headers: {
                "Content-Type": "application/json"
            }
        }
    ).then(response => {
        Cookies.set("token", response.data.token);
    });
};