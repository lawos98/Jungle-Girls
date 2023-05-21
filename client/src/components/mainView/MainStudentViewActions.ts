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

export const updateReadStudentNotification = async (notificationId: number) => {
    try {
        const response = await api.put("/student-notification/update/"+notificationId);
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

export const getEvents = (callback:any) => {
    api.get(`/activity/temporary-events`).
    then(response => {
        console.log(response.data)
        callback(response.data);
    });
}

export interface EventResponse {
    name: string;
    duration: string;
    startDate: string;
}