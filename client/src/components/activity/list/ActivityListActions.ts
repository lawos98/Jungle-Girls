import api from "../../../api/axios";

export const getActivities = () => {
    return api
        .get("/activity")
        .then((response: { data }) => {
            console.log(response);
            return response.data;
        })
}

export const deleteActivity = (payload) => {
    return api
        .put("/activity/delete/" + payload.id, payload)
        .then((response) => {
            console.log(response);
            return response.data;
        })
}