import React, {useEffect} from "react";
import "./App.css";
import Login from "./components/login/Login";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Register from "./components/register/Register";
import EditGrades from "./components/editGrades/EditGrades";
import PrivateRoute from "./utils/PrivateRoute";
import * as actions from "./components/login/LoginActions";
import toast, { Toaster } from 'react-hot-toast';
import StudentGrades from "./components/studentGrades/StudentGrades";


function App() {
    useEffect(() => {
        actions.login("admin", "admin");
    });
    return (
        <Router>
            <Toaster />
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
            </Routes>
        </Router>
    );
}

export default App;
