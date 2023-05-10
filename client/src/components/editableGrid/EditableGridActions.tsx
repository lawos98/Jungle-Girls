import api from "../../api/axios";
import { ActivityScoreList } from "../types/EditableGridTypes";


export const getGrades = (id:number,callback:any) => {
    api.get(`/score/${id}`).
        then(response => {
            callback(response.data);
        });
};
export const updateGrades = (id: number, updatedScores: Array<ActivityScoreList>) => {
    return api.put(`/score/${id}`, updatedScores);
};

export const getCategories = (activityCategoryIds: Array<number>, callback: any) => {
    api.get("activity-category").then((response) => {
        // Return only the categories that are in the activityCategoryIds array
        // in format of: Array<{ id: number; name: string }>
        const filteredCategories = response.data
            .filter((category: any) => activityCategoryIds.includes(category.id))
            .map((category: any) => ({
                id: category.id,
                name: category.name,
            }));
        callback(filteredCategories);
    });
};