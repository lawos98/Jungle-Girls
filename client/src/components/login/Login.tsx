import React, {useEffect, useState} from "react";
import * as actions from "./LoginActions";
import { inputStyle, labelStyle, buttonStyle, formStyle, errorStyle } from "../../utils/formStyles";
import {useFormik} from "formik";
import * as Yup from "yup";
import {useDispatch, useSelector} from "react-redux";
import {setUser} from "../../reducers/UserReducer";
import {useNavigate} from "react-router-dom";

const Login: React.FC = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [serverError, setServerError] = useState("");
    const user = useSelector((state: any) => state.user);


    const validationSchema = Yup.object({
        nick: Yup.string().required("Nazwa użytkownika jest wymagana"),
        password: Yup.string().required("Hasło jest wymagane"),
    });

    const successCallback = (userData: any) => {
        actions.getDescription((userDesc:any) => {
            dispatch(setUser({...userData, ...userDesc}));
            navigate("/");
        }, (message:any) => {
            dispatch(setUser(userData));
            navigate("/");
        });
    };

    useEffect(() => {
        if (user.isLogged) {
            if (user.courseGroupId) {
                navigate("/");
            }
            else {
                navigate("/secret-code");
            }
        }
    },[]);


    const formik = useFormik({
        initialValues: {
            nick: "",
            password: "",
        },
        onSubmit: values => {
            actions.login(values.nick, values.password, successCallback ,(message:string) => {
                setServerError(message);
            });
        },
        validationSchema: validationSchema,
    });

    useEffect(() => {
        setServerError("");
    }, [formik.values.nick, formik.values.password]);

    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center">
            <form
                onSubmit={formik.handleSubmit}
                className={formStyle}
            >
                <h2 className="text-2xl font-bold mb-6">Logowanie</h2>
                <div className="mb-4">
                    <label
                        htmlFor="nick"
                        className={labelStyle}>
                        Nazwa użytkownika
                    </label>
                    <input
                        id="nick"
                        type="text"
                        value={formik.values.nick}
                        onChange={formik.handleChange}
                        className={inputStyle}
                    />
                    {formik.touched.nick && formik.errors.nick && (
                        <div className={errorStyle}>{formik.errors.nick}</div>
                    )}
                </div>

                <div className="mb-6">
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

                <button
                    type="submit"
                    className={buttonStyle}
                >
                    Zaloguj się
                </button>
                {serverError && (
                    <div className={errorStyle}>{serverError}</div>
                )}
                <div className="mt-4">
                    <span className="text-gray-600">Nie masz konta? </span>
                    <a
                        href="/register"
                        className="text-indigo-600 hover:text-indigo-800 font-medium text-center"
                    >
                        Zarejestruj się
                    </a>
                </div>
            </form>
        </div>
    );
};

export default Login;

