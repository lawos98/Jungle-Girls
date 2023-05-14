import React, { useEffect, useState } from "react";
import * as actions from "./MessagesActions";
import {buttonStyle, errorStyle, formStyle, inputStyle, labelStyle} from "../../utils/formStyles";
import {Duration, DateTime} from "luxon";
import Cookies from "js-cookie";
import {useFormik} from "formik";
import * as Yup from "yup";
import moment from "moment";
import toast from "react-hot-toast";
import * as formUtils from "../activity/common/FormUtils";

const SendMessage= () => {
    const authToken = Cookies.get("token");

    useEffect(() => {
    }, []);

    const validationSchema = Yup.object({
        title: Yup.string()
            .required("Tytuł wiadomości jest wymagany"),
        text: Yup.string().required("Treść wiadomości jest wymagana"),
    });


    const formik = useFormik({
        initialValues: {
            title: "",
            text: "",
        },
        onSubmit: (values, { resetForm }) => {
            const payload = {
                "title": values.title,
                "text": values.text,
                "token": authToken,
            }
            const sendMessagePromise = actions.sendMessage(payload);

            toast.promise(sendMessagePromise, {
                loading: "Wysyłam wiadomość...",
                success: "Wiadomość wysłana!",
                error: "Błąd podczas wysyłania wiadomości",
            }).then(() => {
                resetForm();
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

                <h2 className="text-2xl font-bold mb-6">Wyślij wiadomość do wszystkich studentów</h2>

            <div className="mb-4">
                <label
                    htmlFor="name"
                    className={labelStyle}
                >
                    Tytuł
                </label>

                <input
                    type="text"
                    id="title"
                    value={formik.values.title}
                    onChange={formik.handleChange}
                    className={inputStyle}
                />
                {formik.touched.title && formik.errors.title && (
                    <div className={errorStyle}>{formik.errors.title}</div>
                )}
            </div>

                <div className="mb-4">
                    {" "}
                    <label
                        htmlFor="text"
                        className={labelStyle}
                    >
                        Opis:
                    </label>
                    <textarea
                        id="text"
                        name="text"
                        value={formik.values.text}
                        onChange={formik.handleChange}
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    ></textarea>
                    {formik.touched.text && formik.errors.text && (
                        <div className={errorStyle}>{formik.errors.text}</div>
                    )}
                </div>


            <button
                type="submit"
                className={buttonStyle}
            >
                Wyślij
            </button>
        </form>
        </div>
    );
};

export default SendMessage;
