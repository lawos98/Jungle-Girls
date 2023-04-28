import api from "../../../api/axios";
import { Duration } from "luxon";


export const getActivityCategories = () => {
    return api
        .get("/activity-category", )
        .then((response) => {
            return response.data;
        }).catch(error => {
            if (error.response) {
                errorCallback(error.response.data.message);
            }
        });
}

export const getActivities = () => {
    return api
        .get("/activity", ).then((response) => {
        return response;
    }).catch(error => {
            if (error.response) {
                errorCallback(error.response.data.message);
            }
        });
}

export const editActivity = (
    activityId: string,
    activityName: string,
    maxScore: number,
    description: string,
    duration: Duration,
    activityTypeName: string,
    activityCategoryName: string,
    courseGroupNames: Array<string>,
    courseGroupStartDates: Array<Date>,
    token: string | undefined
) => {
  const payload = {
    id: activityId,
    name: activityName,
    maxScore: maxScore,
    description: description,
    duration: duration,
    activityTypeName: activityTypeName,
    activityCategoryName: activityCategoryName,
    courseGroupNames: courseGroupNames,
    courseGroupStartDates: courseGroupStartDates,
  };

  api
      .put("/activity/edit/" + activityId, payload, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        console.log(response);
      });
};
