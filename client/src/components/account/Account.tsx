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
            .url("Podaj prawidłowy adres URL")
            .required("Link do GitHub'a jest wymagany"),
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
            studentIndex: "",
            githubLink: "",
        },
        onSubmit: (values) => {
            const updateAccountPromise = actions.updateAccount(values.studentIndex, values.githubLink);

            toast.promise(updateAccountPromise, {
                loading: "Aktualizowanie danych...",
                success: "Dane zostały zaktualizowane",
                error: (err) => `Wystąpił błąd: ${err.response.data.message}`,
            });

            updateAccountPromise
                .then((response) => {
                    formik.setFieldValue("studentIndex", response.data.index ? response.data.index : "");
                    formik.setFieldValue("githubLink", response.data.githubLink ? response.data.githubLink : "");
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
                        <div className={errorStyle}>{formik.errors.studentIndex}</div>
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
                        <div className={errorStyle}>{formik.errors.githubLink}</div>
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
