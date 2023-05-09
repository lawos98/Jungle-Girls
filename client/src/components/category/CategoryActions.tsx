import api from "./../../api/axios";

export const createCategory = (
    categoryName: string,
    description: string,
    token: string | undefined
) => {
    const payload = {
        name: categoryName,
        description: description,
    };

    return api
        .post("/activity-category/create", payload, {
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },
        })
        .then((response: any) => {
            console.log(response);
            return response;
        });
};


export const deleteCategory = (payload) => {
    return api
        .put("/activity-category/delete/" + payload.id, payload )
        .then((response) => {
            console.log(response);
            return response;
        });
};

export const getCategories = () => {
    return api
        .get("/activity-category")
        .then((response) => {
            console.log(response);
            return response.data;
        });
};

export const editCategory = (payload) => {
    return api
        .put("/activity-category/edit/" + payload.id, payload)
        .then((response) => {
            console.log(response);
            return response;
        });
};