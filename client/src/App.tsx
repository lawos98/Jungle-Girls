import React from "react";
import "./App.css";
import Login from "./components/login/Login";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Register from "./components/register/Register";
import EditGrades from "./components/editGrades/EditGrades";
import PrivateRoute from "./utils/PrivateRoute";
import {useSelector} from "react-redux";

import StudentNavbar from "./components/navbars/StudentNavbar";
import TeacherNavbar from "./components/navbars/TeacherNavbar";


function App() {
    const user = useSelector((state: any) => state.user);

    const getProperNavbar = () => {
        if (user.roleId === 1)
        {
            return null;
        }
        else if (user.roleId === 2)
        {
            return (
                <StudentNavbar/>
            );
        }
        else if (user.roleId === 3 || user.roleId === 4)
        {
            return (
                <TeacherNavbar/>
            );
        }
    }

    return (
        <Router>
            {getProperNavbar()}
            <Routes>
                <Route path="/editGrades" element={<EditGrades />} />
                <Route path="/login" element={<Login/>}/>
                <Route path="/register" element={<Register/>}/>
                <Route path="/" element={
                    <PrivateRoute>
                        <h1>Home</h1>
                    </PrivateRoute>
                }/>
            </Routes>
        </Router>
    );
}

export default App;
