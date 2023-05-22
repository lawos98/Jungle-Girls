import { createSlice } from "@reduxjs/toolkit";

const userSlice = createSlice({
    name: "user",
    initialState: {
        userId: "",
        roleId: "",
        username: "",
        firstname: "",
        lastname: "",
        index: "",
        githubLink: "",
        courseGroupId: "",
        isLogged: false,
    },
    reducers: {
        setUser: (state, action) => {
            state.userId = action.payload.userId;
            state.roleId = action.payload.roleId;
            state.username = action.payload.username;
            state.firstname = action.payload.firstname;
            state.lastname = action.payload.lastname;
            state.index = action.payload.index;
            state.githubLink = action.payload.githubLink;
            state.courseGroupId = action.payload.courseGroupId;
            state.isLogged = true;
        },
        clearUser: (state) => {
            state.userId = "";
            state.roleId = "";
            state.firstname = "";
            state.lastname = "";
            state.isLogged = false;
        },
    },
});

export const { setUser, clearUser } = userSlice.actions;
export default userSlice.reducer;