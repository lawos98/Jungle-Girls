import React, { useEffect, useState } from "react";
import { inputStyle, labelStyle, buttonStyle, formStyle, errorStyle } from "../../utils/formStyles";
import { useFormik } from "formik";
import * as Yup from "yup";
import { useDispatch, useSelector } from "react-redux";
import { setUser } from "../../reducers/UserReducer";
import { useNavigate } from "react-router-dom";
import * as actions from "./AccountActions";
import toast from "react-hot-toast";

const Account: React.FC = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [serverError, setServerError] = useState("");
    const user = useSelector((state: any) => state.user);

    const validationSchema = Yup.object({
        studentIndex: Yup.string().required("Indeks studenta jest wymagany"),
        githubLink: Yup.string()
            .url("Podaj prawidłowy adres URL"),
        username: Yup.string().required("Nazwa użytkownika jest wymagana"),
        firstname: Yup.string().required("Imię jest wymagane"),
        lastname: Yup.string().required("Nazwisko jest wymagane"),
    });


    useEffect(() => {
        actions.getAccount((userData: any) => {
            formik.setFieldValue("studentIndex", userData.index ? userData.index : "");
            formik.setFieldValue("githubLink", userData.githubLink ? userData.githubLink : "");
        }, (message: string) => {
            setServerError(message);
        });
    }, []);

    const formik = useFormik({
        initialValues: {
            studentIndex: user.index,
            githubLink: user.githubLink,
            username: user.username,
            firstname: user.firstname,
            lastname: user.lastname,
        },
        onSubmit: (values) => {
            const updateAccountPromise = actions.updateAccount(values.studentIndex, values.githubLink, values.username, values.firstname, values.lastname);

            toast.promise(updateAccountPromise, {
                loading: "Aktualizowanie danych...",
                success: "Dane zostały zaktualizowane",
                error: (err) => `Wystąpił błąd: ${err.response.data.message}`,
            });

            updateAccountPromise
                .then((response) => {
                    dispatch(setUser({...response[0].data, ...response[1].data}));
                    setServerError("");
                })
                .catch((error) => {
                    setServerError(error.response.data.message);
                });
        },
        validationSchema: validationSchema,
    });

    useEffect(() => {
        setServerError("");
    }, [formik.values.studentIndex, formik.values.githubLink]);

    return (
        <div className="min-h-max bg-gray-100 flex items-center justify-center">
            <form onSubmit={formik.handleSubmit} className={formStyle}>
                <h2 className="text-2xl font-bold mb-6">Dane konta</h2>
                <div className="mb-4">
                    <label htmlFor="studentIndex" className={labelStyle}>
                        Indeks studenta
                    </label>
                    <input
                        id="studentIndex"
                        type="text"
                        value={formik.values.studentIndex}
                        onChange={formik.handleChange}
                        className={inputStyle}
                    />
                    {formik.touched.studentIndex && formik.errors.studentIndex && (
                        <div className={errorStyle}>{formik.errors.studentIndex.toString()}</div>
                    )}
                </div>

                <div className="mb-6">
                    <label htmlFor="githubLink" className={labelStyle}>
                        Link do GitHub'a
                    </label>
                    <input
                        id="githubLink"
                        type="text"
                        value={formik.values.githubLink}
                        onChange={formik.handleChange}
                        className={inputStyle}
                    />
                    {formik.touched.githubLink && formik.errors.githubLink && (
                        <div className={errorStyle}>{formik.errors.githubLink.toString()}</div>
                    )}
                </div>

                <div className="mb-6">
                    <label htmlFor="username" className={labelStyle}>
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
                        <div className={errorStyle}>{formik.errors.username.toString()}</div>
                    )}
                </div>

                <div className="mb-6">
                    <label htmlFor="firstname" className={labelStyle}>
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
                        <div className={errorStyle}>{formik.errors.firstname.toString()}</div>
                    )}
                </div>

                <div className="mb-6">
                    <label htmlFor="lastname" className={labelStyle}>
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
                        <div className={errorStyle}>{formik.errors.lastname.toString()}</div>
                    )}
                </div>

                <button type="submit" className={buttonStyle}>
                    Aktualizuj dane
                </button>
                {serverError && <div className={errorStyle}>{serverError}</div>}
            </form>
        </div>
    );
};

export default Account;
