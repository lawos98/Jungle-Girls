import api from "../../api/axios";
import { ActivityScoreList } from '../types/EditableGridTypes';


export const getGrades = (callback:any) => {
    api.get(`/score`).
    then(response => {
        callback(response.data);
    });
}