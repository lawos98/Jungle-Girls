import React, {useEffect, useState} from "react";
import * as actions from "./MessagesActions";
import {buttonStyle, errorStyle, formStyle, inputStyle, labelStyle} from "../../utils/formStyles";
import Cookies from "js-cookie";
import {useFormik} from "formik";
import * as Yup from "yup";
import toast from "react-hot-toast";

const SendMessage= () => {
    const [students, setStudents] = useState([]);
    const [groups, setGroups] = useState([]);
    const [selectedValues, setSelectedValues] = useState([]);
    const [selectedOption, setSelectedOption] = useState('students');
    const [selectAll, setSelectAll] = useState(false);
    const [options, setOptions] = useState({
        students: students,
        courseGroups: groups,
    });
    const authToken = Cookies.get("token");

    useEffect(() => {
        const fetchData = async () => {
            try {
                const studentsData = await actions.getStudents(authToken);
                const initializedStudents = studentsData.map(student => ({ ...student, checked: false }));
                setStudents(initializedStudents);

                const groupsData = await actions.getGroups(authToken);
                const initializedGroups = groupsData.map(group => ({ ...group, checked: false }));
                setGroups(initializedGroups);

                setOptions({students: initializedStudents, courseGroups: initializedGroups});
                console.log(options);
            } catch (error) {
                console.error("Error fetching data:", error);
            }
        };
        fetchData();
    }, []);


    const validationSchema = Yup.object({
        subject: Yup.string()
            .required("Temat wiadomości jest wymagany"),
        content: Yup.string().required("Treść wiadomości jest wymagana"),
        selectedValues: Yup.array()
            .test("selectedValues", "Wybierz co najmniej jednego odbiorcę", (value) => {
                return selectedValues && selectedValues.length > 0;
            })
    });


    const formik = useFormik({
        initialValues: {
            subject: "",
            content: "",
        },
        onSubmit: (values, { resetForm }) => {
            console.log(selectedOption,selectedValues)
            let payload;
            let sendMessagePromise;
            if(selectedOption == "students"){
                payload = {
                    "subject": values.subject,
                    "content": values.content,
                    "studentIds": selectedValues,
                    "token": authToken,
                }
                sendMessagePromise = actions.sendMessage(payload);
            }
            else{
                payload = {
                    "subject": values.subject,
                    "content": values.content,
                    "groupIds": selectedValues,
                    "token": authToken,
                }
                sendMessagePromise = actions.sendGroupMessage(payload);
            }

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


    const handleOptionChange = (event) => {
        setSelectedOption(event.target.value);
        setSelectedValues([]);
        setSelectAll(false);
        setOptions({
            students: students,
            courseGroups: groups,
        })
    };

    const handleCheckboxChange = (recipients, checkboxId) => {
        const updatedOptions = { ...options };
        const updatedCheckboxValues = updatedOptions[recipients].map((checkbox) => {
            if (checkbox.id === checkboxId) {
                return { ...checkbox, checked: !checkbox.checked };
            }
            return checkbox;
        });
        updatedOptions[recipients] = updatedCheckboxValues;
        setOptions(updatedOptions);

        const selectedCheckbox = updatedCheckboxValues.find((checkbox) => checkbox.id === checkboxId);
        if (selectedCheckbox.checked) {
            setSelectedValues([...selectedValues, selectedCheckbox.id]);
        } else {
            setSelectedValues(selectedValues.filter((value) => value !== selectedCheckbox.id));
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

        const selectedValues = selectAll ? [] : updatedCheckboxValues.map((checkbox) => checkbox.id);
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
                        style={{ marginRight: '10px'}}
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
                        style={{ marginRight: '10px' }}
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
                        className={`mb-2 mt-4 ${labelStyle}`}
                    >
                        Wybierz adresatów:
                    </label>

                    {selectedOption && (
                        <div>
                            <div style={{ maxHeight: '100px', overflowY: 'scroll' }}>
                                {options[selectedOption].map((checkbox) => (
                                    <div key={checkbox.id} >
                                        <input style={{ marginRight: '10px' }}
                                            type="checkbox"
                                            checked={checkbox.checked}
                                            onChange={() => handleCheckboxChange(selectedOption, checkbox.id)}
                                        />
                                        {selectedOption == "students" ? checkbox.firstname + ' ' + checkbox.lastname : checkbox.name}
                                    </div>
                                ))}
                            </div>
                            <div className="mt-2">
                                <a onClick={handleToggleSelectAll}
                                        className="text-indigo-600 hover:text-indigo-800 font-medium text-center "
                                >
                                    {selectAll ? 'Odznacz wszystkie' : 'Zaznacz wszystkie'}
                                </a>
                            </div>
                        </div>
                    )}

                    {formik.errors.selectedValues && (
                        <div className={errorStyle}>{formik.errors.selectedValues}</div>
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
                        {formik.touched.subject && formik.errors.subject && (
                            <div className={errorStyle}>{formik.errors.subject}</div>
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
                        {formik.touched.content && formik.errors.content && (
                            <div className={errorStyle}>{formik.errors.content}</div>
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
