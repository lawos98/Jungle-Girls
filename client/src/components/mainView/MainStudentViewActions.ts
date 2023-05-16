import api from "../../api/axios";

export const getStudentNotifications = async (): Promise<StudentNotificationResponse[]> => {
    try {
        const response = await api.get("/student-notification");
        console.log(response.data);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export interface StudentNotificationResponse {
    id: number;
    date: string;
    subject: string;
    content: string;
    author: string;
    wasRead: boolean;
}

export const getScore = (callback:any) => {
    api.get(`/score/total-score`).
    then(response => {
        console.log(response.data)
        callback(response.data);
    });
};

export interface ScoreResponse {
    maxPoints: number;
    points: number;
}