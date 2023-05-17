import api from "../../api/axios";

export const getGroups = (callback:any) => {
    api.get(`/instructor`).
    then(response => {
        console.log(response.data.groupIds)
        callback(response.data);
    });
};