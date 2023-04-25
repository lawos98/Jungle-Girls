import api from "../../api/axios";

export const joinGroup = (secretCode: string, successCallback:Function, errorCallback:Function ) => {
    const payload = {
        code : secretCode
    };
    api.patch("/role/secret-code", payload,
        {
            headers: {
                "Content-Type": "application/json"
            }
        }
    ).then(response => {
        successCallback(response.data);
    }).catch(error => {
        console.log(error);
    });
}