import api from "../../api/axios";

export const sendMessage = async payload => {
    try {
        const response = await api.post("/student-notification/create",payload);
        console.log(response.data);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const sendGroupMessage = async payload => {
    try {
        const response = await api.post("/group-notification/create",payload);
        console.log(response.data);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const getStudents = async (token) => {
    try {
        const response = await api.get("/student-notification/create", {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                }
        })
        console.log(response.data);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const getGroups = async (token) => {
    try {
        const response = await api.get("/group-notification/create", {
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            }
        })
        console.log(response.data);
        return response.data;
    } catch (error) {
        throw error;
    }
};
