import api from "../../api/axios";
import { ActivityScoreList } from '../types/EditableGridTypes';


export const getGrades = (id:number,callback:any) => {
    api.get(`/score/${id}`).
    then(response => {
        callback(response.data);
    });
}
export const updateGrades = (id: number, updatedScores: Array<ActivityScoreList>) => {
    return api.put(`/score/${id}`, updatedScores);
};