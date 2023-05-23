import api from "../../api/axios";


export const updateAccount = (index: string, link: string, username: string, firstname: string, lastname: string) => {
    const payload = {
        index : index,
        githubLink : link,
    };

    const payload2 = {
        username : username,
        firstname : firstname,
        lastname : lastname
    }

    const prom1 = api.patch("/user", payload2,);

    const prom2 = api.patch("/student-description", payload,);

    return Promise.all([prom1, prom2]);
};

export const getAccount = (successCallback:Function, errorCallback:Function ) => {
    api.get("/student-description").then(response => {
        successCallback(response.data);
    }).catch(error => {
        errorCallback(error.response.data.message);
    });
};