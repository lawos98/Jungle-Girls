import React, { useState, useEffect } from "react";
import { inputStyle, labelStyle, buttonStyle, formStyle, errorStyle } from "../../utils/formStyles";
import { useFormik } from "formik";
import * as Yup from "yup";
import { useNavigate } from "react-router-dom";
import * as actions from "./SecretCodeActions";
import {useDispatch, useSelector} from "react-redux";
import {setUser, clearUser} from "../../reducers/UserReducer";

const SecretCode: React.FC = () => {
    const navigate = useNavigate();
    const [serverError, setServerError] = useState("");
    const dispatch = useDispatch();
    const user = useSelector((state: any) => state.user);

    const validationSchema = Yup.object({
        secretCode: Yup.string().required("Sekretny kod jest wymagany"),
    });

    const successCallback = (userInfo:any) => {
        dispatch(setUser({...user,...userInfo, roleId:2}));
        navigate("/");
    };

    const handleLogout = () => {
        dispatch(clearUser());
        navigate("/login");
    };

    const formik = useFormik({
        initialValues: {
            secretCode: "",
        },
        onSubmit: (values) => {
            actions.joinGroup(values.secretCode, successCallback, (message: string) => {
                setServerError(message);
            });
        },
        validationSchema: validationSchema,
    });

    useEffect(() => {
        setServerError("");
    }, [formik.values.secretCode]);

    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center">
            <form onSubmit={formik.handleSubmit} className={formStyle}>
                <h2 className="text-2xl font-bold mb-6">Dołącz do grupy</h2>
                <div className="mb-6">
                    <label htmlFor="secretCode" className={labelStyle}>
                        Sekretny kod
                    </label>
                    <input
                        id="secretCode"
                        type="text"
                        value={formik.values.secretCode}
                        onChange={formik.handleChange}
                        className={inputStyle}
                    />
                    {formik.touched.secretCode && formik.errors.secretCode && (
                        <div className={errorStyle}>{formik.errors.secretCode}</div>
                    )}
                </div>
                <button type="submit" className={buttonStyle}>
                    Dołącz
                </button>
                {serverError && <div className={errorStyle}>{serverError}</div>}
                <button
                    onClick={handleLogout}
                    className="mt-4 text-xs text-blue-500 hover:text-blue-700"
                >
                    Wyloguj się
                </button>
            </form>

        </div>
    );
};

export default SecretCode;
