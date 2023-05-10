import api from "../../api/axios";


export const updateAccount = (index: string, link: string) => {
    const payload = {
        index : index,
        githubLink : link
    };
    return api.patch("/student-description", payload,
        {
            headers: {
                "Content-Type": "application/json"
            }
        }
    );
};

export const getAccount = (successCallback:Function, errorCallback:Function ) => {
    api.get("/student-description").then(response => {
        successCallback(response.data);
    }).catch(error => {
        errorCallback(error.response.data.message);
    });
};