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