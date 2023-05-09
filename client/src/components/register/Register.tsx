import React, {useEffect, useState} from "react";
import * as actions from "./RegisterActions";
import { inputStyle, labelStyle, buttonStyle, formStyle, errorStyle } from "../../utils/formStyles";
import { useFormik } from "formik";
import * as Yup from "yup";
import {setUser} from "../../reducers/UserReducer";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";

const Register: React.FC = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [serverError, setServerError] = useState("");
    const user = useSelector((state: any) => state.user);

    useEffect(() => {
        if (user.isLogged) {
            navigate("/");
        }
    },[]);

    const validationSchema = Yup.object({
        username: Yup.string().required("Nazwa użytkownika jest wymagana"),
        firstname: Yup.string().required("Imię jest wymagane"),
        lastname: Yup.string().required("Nazwisko jest wymagane"),
        password: Yup.string().required("Hasło jest wymagane"),
        confirmPassword: Yup.string()
            .oneOf([Yup.ref("password"), ""], "Hasła muszą być takie same")
            .required("Powtórzenie hasła jest wymagane"),
    });

    const successCallback = (userData: any) => {
        dispatch(setUser(userData));
        navigate("/");
    };

    const formik = useFormik({
        initialValues: {
            username: "",
            firstname: "",
            lastname: "",
            password: "",
            confirmPassword: "",
        },
        onSubmit: values => {
            actions.register(values.username, values.firstname, values.lastname, values.password,successCallback ,(message:string) => {
                setServerError(message);
            });
        },
        validationSchema: validationSchema,
    });

    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center">
            <form
                onSubmit={formik.handleSubmit}
                className={formStyle}
            >
                <h2 className="text-2xl font-bold mb-6">Rejestracja</h2>
                <div className="mb-4">
                    <label
                        htmlFor="username"
                        className={labelStyle}
                    >
                        Nazwa użytkownika
                    </label>
                    <input
                        id="username"
                        type="text"
                        value={formik.values.username}
                        onChange={formik.handleChange}
                        className={inputStyle}
                    />
                    {formik.touched.username && formik.errors.username && (
                        <div className={errorStyle}>{formik.errors.username}</div>
                    )}
                </div>
                <div className="mb-4">
                    <label
                        htmlFor="firstname"
                        className={labelStyle}
                    >
                        Imię
                    </label>
                    <input
                        id="firstname"
                        type="text"
                        value={formik.values.firstname}
                        onChange={formik.handleChange}
                        className={inputStyle}
                    />
                    {formik.touched.firstname && formik.errors.firstname && (
                        <div className={errorStyle}>{formik.errors.firstname}</div>
                    )}
                </div>
                <div className="mb-4">
                    <label
                        htmlFor="lastname"
                        className={labelStyle}
                    >
                        Nazwisko
                    </label>
                    <input
                        id="lastname"
                        type="text"
                        value={formik.values.lastname}
                        onChange={formik.handleChange}
                        className={inputStyle}
                    />
                    {formik.touched.lastname && formik.errors.lastname && (
                        <div className={errorStyle}>{formik.errors.lastname}</div>
                    )}
                </div>
                <div className="mb-4">
                    <label
                        htmlFor="password"
                        className={labelStyle}
                    >
                        Hasło
                    </label>
                    <input
                        id="password"
                        type="password"
                        value={formik.values.password}
                        onChange={formik.handleChange}
                        className={inputStyle}
                    />
                    {formik.touched.password && formik.errors.password && (
                        <div className={errorStyle}>{formik.errors.password}</div>
                    )}
                </div>
                <div className="mb-6">
                    <label
                        htmlFor="confirmPassword"
                        className={labelStyle}
                    >
                        Powtórz hasło
                    </label>
                    <input
                        id="confirmPassword"
                        type="password"
                        value={formik.values.confirmPassword}
                        onChange={formik.handleChange}
                        className={inputStyle}
                    />
                    {formik.touched.confirmPassword && formik.errors.confirmPassword && (
                        <div className={errorStyle}>{formik.errors.confirmPassword}</div>
                    )}
                </div>
                <button
                    type="submit"
                    className={buttonStyle}
                >
                    Zarejestruj się
                </button>
                {serverError && (
                    <div className={errorStyle}>{serverError}</div>
                )}
                <div className="mt-4">
                    <span className="text-gray-600">Masz już konto? </span>
                    <a
                        href="/login"
                        className="text-indigo-600 hover:text-indigo-800 font-medium text-center"
                    >
                        Zaloguj się
                    </a>
                </div>
            </form>
        </div>
    );
};

export default Register;
