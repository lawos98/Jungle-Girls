import React, {useEffect, useState} from "react";
import * as actions from "./MessagesActions";
import {buttonStyle, errorStyle, formStyle, inputStyle, labelStyle} from "../../utils/formStyles";
import Cookies from "js-cookie";
import {useFormik} from "formik";
import * as Yup from "yup";
import toast from "react-hot-toast";

const SendMessage= () => {
    const authToken = Cookies.get("token");

    useEffect(() => {
    }, []);

    const validationSchema = Yup.object({
        subject: Yup.string()
            .required("Temat wiadomości jest wymagany"),
        content: Yup.string().required("Treść wiadomości jest wymagana"),
    });


    const formik = useFormik({
        initialValues: {
            subject: "",
            content: "",
        },
        onSubmit: (values, { resetForm }) => {
            const payload = {
                "subject": values.subject,
                "content": values.content,
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

    const [selectedValues, setSelectedValues] = useState([]);
    const [selectedOption, setSelectedOption] = useState('students');
    const [selectAll, setSelectAll] = useState(false);
    const [options, setOptions] = useState({
        students: [
            { id: 1, value: 'Option 1.1', checked: false },
            { id: 2, value: 'Option 1.2', checked: false },
            { id: 3, value: 'Option 1.3', checked: false },
            { id: 4, value: 'Option 1.4', checked: false },
            { id: 5, value: 'Option 1.5', checked: false },
            { id: 6, value: 'Option 1.6', checked: false },
        ],
        courseGroups: [
            { id: 1, value: 'Option 2.1', checked: false },
            { id: 2, value: 'Option 2.2', checked: false },
            { id: 3, value: 'Option 2.3', checked: false },
        ],
    });

    const handleOptionChange = (event) => {
        setSelectedOption(event.target.value);
        setSelectedValues([]);
        setSelectAll(false);
        setOptions({
            students: [
                { id: 1, value: 'Option 1.1', checked: false },
                { id: 2, value: 'Option 1.2', checked: false },
                { id: 3, value: 'Option 1.3', checked: false },
                { id: 4, value: 'Option 1.4', checked: false },
                { id: 5, value: 'Option 1.5', checked: false },
                { id: 6, value: 'Option 1.6', checked: false },
            ],
            courseGroups: [
                { id: 1, value: 'Option 2.1', checked: false },
                { id: 2, value: 'Option 2.2', checked: false },
                { id: 3, value: 'Option 2.3', checked: false },
            ],
        })
    };

    const handleCheckboxChange = (listKey, checkboxId) => {
        const updatedOptions = { ...options };
        const updatedCheckboxValues = updatedOptions[listKey].map((checkbox) => {
            if (checkbox.id === checkboxId) {
                return { ...checkbox, checked: !checkbox.checked };
            }
            return checkbox;
        });
        updatedOptions[listKey] = updatedCheckboxValues;
        setOptions(updatedOptions);

        const selectedCheckbox = updatedCheckboxValues.find((checkbox) => checkbox.id === checkboxId);
        if (selectedCheckbox.checked) {
            setSelectedValues([...selectedValues, selectedCheckbox.value]);
        } else {
            setSelectedValues(selectedValues.filter((value) => value !== selectedCheckbox.value));
        }
    };

    const handleToggleSelectAll = () => {
        const updatedOptions = { ...options };
        const updatedCheckboxValues = updatedOptions[selectedOption].map((checkbox) => ({
            ...checkbox,
            checked: !selectAll,
        }));
        updatedOptions[selectedOption] = updatedCheckboxValues;
        setOptions(updatedOptions);

        const selectedValues = selectAll ? [] : updatedCheckboxValues.map((checkbox) => checkbox.value);
        setSelectedValues(selectedValues);
        setSelectAll(!selectAll);
    };


    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center">
        <form
            onSubmit={formik.handleSubmit}
            className={formStyle}
        >

                <h2 className="text-2xl font-bold mb-6">Wyślij wiadomość</h2>

            <div>
                <input
                    type="radio"
                    name="option"
                    value="students"
                    checked={selectedOption === 'students'}
                    onChange={handleOptionChange}
                />
                Do studentów
            </div>
            <div>
                <input
                    type="radio"
                    name="option"
                    value="courseGroups"
                    checked={selectedOption === 'courseGroups'}
                    onChange={handleOptionChange}
                />
                Do grup
            </div>

            <div className="mb-4 mt-4">
                <label
                    htmlFor="subject"
                    className={labelStyle}
                >
                    Wybierz adresatów:
                </label>

                {selectedOption && (
                    <div>
                        <div style={{ maxHeight: '100px', overflowY: 'scroll' }}>
                            {options[selectedOption].map((checkbox) => (
                                <div key={checkbox.id}>
                                    <input
                                        type="checkbox"
                                        checked={checkbox.checked}
                                        onChange={() => handleCheckboxChange(selectedOption, checkbox.id)}
                                    />
                                    {checkbox.value}
                                </div>
                            ))}
                        </div>
                        <div>
                            <button onClick={handleToggleSelectAll}
                                className="text-indigo-600 hover:text-indigo-800 font-medium text-center mb-4"
                            >
                                {selectAll ? 'Odznacz wszystkie' : 'Zaznacz wszystkie'}
                            </button>
                        </div>
                        <div>
                            <p>Wybrane wartości:</p>
                            {selectedValues.map((value) => (
                                <div key={value}>{value}</div>
                            ))}
                        </div>
                    </div>
                )}

            <div className="mb-4 mt-4">
                <label
                    htmlFor="subject"
                    className={labelStyle}
                >
                    Temat:
                </label>

                <input
                    type="text"
                    id="subject"
                    value={formik.values.subject}
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
                        Treść:
                    </label>
                    <textarea
                        id="content"
                        name="content"
                        value={formik.values.content}
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
            </button></div>
        </form>
        </div>
    );
};

export default SendMessage;
