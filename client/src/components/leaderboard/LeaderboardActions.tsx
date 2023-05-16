import api from "../../api/axios";


export const getLeaderboard = (callback:any) => {
    api.get(`/score/leaderboard`).
    then(response => {
        console.log(response.data);
        callback(response.data);
    });
};