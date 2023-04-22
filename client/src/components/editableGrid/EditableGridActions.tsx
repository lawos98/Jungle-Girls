import api from "../../api/axios";

export const getGrades = (id:number,callback:any) => {
    api.get(`/score/${id}`).then(response => {
        callback(response.data);
    });
}