import { createSlice } from '@reduxjs/toolkit';

const userSlice = createSlice({
    name: 'user',
    initialState: {
        userId: '',
        roleId: '',
        firstname: '',
        lastname: '',
        isLogged: false,
    },
    reducers: {
        setUser: (state, action) => {
            state.userId = action.payload.userId;
            state.roleId = action.payload.roleId;
            state.firstname = action.payload.firstname;
            state.lastname = action.payload.lastname;
            state.isLogged = true;
        },
        clearUser: (state) => {
            state.userId = '';
            state.roleId = '';
            state.firstname = '';
            state.lastname = '';
            state.isLogged = false;
        },
    },
});

export const { setUser, clearUser } = userSlice.actions;
export default userSlice.reducer;