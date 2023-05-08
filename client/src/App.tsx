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
import toast, { Toaster } from 'react-hot-toast';
import StudentGrades from "./components/studentGrades/StudentGrades";
import SecretCode from "./components/account/SecretCode";

import ActivityCreationForm from "./components/activity/create/CreateActivityForm";
import ActivityList from "./components/activity/list/ActivityList";
import Categories from "./components/category/Categories";

function App() {
    const user = useSelector((state: any) => state.user);

    const getProperNavbar = () => {
        if (user.roleId === 1)
        {
            return (
              <SecretCode />
            );
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
            <Toaster />
            {getProperNavbar()}
            <Routes>
                <Route path="/login" element={<Login/>}/>
                <Route path="/register" element={<Register/>}/>
                <Route path="/" element={
                    <PrivateRoute>
                        <h1>Home</h1>
                    </PrivateRoute>
                }/>
                <Route path="/edit-grades" element={
                    <PrivateRoute>
                        <EditGrades/>
                    </PrivateRoute>
                }/>
                <Route path="/grades" element={
                    <PrivateRoute>
                        <StudentGrades/>
                    </PrivateRoute>
                }/>
                <Route
                    path="/create-activity"
                    element={
                        <PrivateRoute>
                            <ActivityCreationForm />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/activity-list"
                    element={
                        <PrivateRoute>
                            <ActivityList />
                        </PrivateRoute>
                    }
                />
                <Route
                    path="/categories"
                    element={
                        <PrivateRoute>
                            <Categories></Categories>
                        </PrivateRoute>
                    }
                />
            </Routes>
        </Router>
    );
}

export default App;
