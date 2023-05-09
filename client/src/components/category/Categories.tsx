import React from "react";
import { useEffect, useState } from "react";
import * as actions from "./CategoryActions";
import Cookies from "js-cookie";
import {buttonStyle, errorStyle, formStyle, inputStyle, labelStyle} from "../../utils/formStyles";
import {useFormik} from "formik";
import * as Yup from "yup";
import toast from "react-hot-toast";

const Categories: React.FC = () => {
    let editedCategory = {
        id: 0,
        name: "",
        description: "",
    };
    const [edited,setEdited] = useState( {
        id: 0,
        name: "",
        description: "",
    });
    const [categories, setCategories] = useState([]);
    const [addFormVisible, setAddFormVisible] = useState(false);
    const [editFormVisible, setEditFormVisible] = useState(false);
    const authToken = Cookies.get("token");

    useEffect(() => {
        actions.getCategories()
            .then((data) => setCategories(data));
    }, []);


    const validationSchemaAdd = Yup.object().shape({
        name: Yup.string()
            .notOneOf(categories.map(category => category.name), "Kategoria o tej nazwie już istnieje")
            .required("Nazwa jest wymagana"),
        description: Yup.string().required("Opis kategorii jest wymagany"),
    });

    const validationSchemaEdit = Yup.object().shape({
        name: Yup.string()
            .notOneOf(categories.filter((category) => category.id !== edited.id)
                .map((category) => category.name),
            "Kategoria o tej nazwie już istnieje")
            .required("Nazwa jest wymagana"),
        description: Yup.string().required("Opis kategorii jest wymagany"),
    });

    const addFormik = useFormik({
        initialValues: {
            name: "",
            description: "",
        },
        validationSchema: validationSchemaAdd,
        onSubmit: values => {
            const createCategoryPromise = actions.createCategory(
                values.name,values.description,authToken);
            toast.promise(createCategoryPromise, {
                loading: "Zapisuję kategorię...",
                success: "Kategoria zapisana!",
                error: "Błąd podczas zapisu kategorii",
            }).then(() => {
                actions.getCategories()
                    .then((response) => {
                        setCategories(response);
                        return response;
                    });
            });
            closeAddForm();
        },
    });

    const editFormik = useFormik({
        initialValues: {
            id: edited.id,
            name: edited.name,
            description: edited.description,
        },
        validationSchema: validationSchemaEdit,
        onSubmit: values => {
            const payload = {
                id: edited.id,
                name: values.name,
                description: values.description,
            };
            const editCategoryPromise = actions.editCategory(payload);
            toast.promise(editCategoryPromise, {
                loading: "Edytuję kategorię...",
                success: "Kategoria zaktualizowana!",
                error: "Błąd podczas edycji kategorii",
            }).then(() => {
                actions.getCategories().then((response) => {
                    setCategories(response);
                    return response;
                });});
            closeEditForm();
        },
    });

    function handleDeleteCategory(id: number) {
        const payload = {
            id: id,
        };
        actions.deleteCategory(payload)
            .then(() => {
                setCategories(categories.filter((category: any) => category.id !== id));
            });
    }

    function showAddForm(): void {
        closeEditForm();
        setAddFormVisible(true);
    }

    function closeAddForm(): void {
        setAddFormVisible(false);
    }

    function showEditForm(id: number): void {
        closeAddForm();
        setEdited(categories.find((category) => category.id == id));
        editedCategory = categories.find((category) => category.id == id);
        editFormik.setFieldValue("name",editedCategory.name);
        editFormik.setFieldValue("description",editedCategory.description);
        setEditFormVisible(true);
    }

    function closeEditForm(): void {
        setEditFormVisible(false);
    }

    return (
        <div className="min-h-screen bg-gray-100 flex  flex-col items-center justify-center">
            <h1 className="text-3xl font-bold mb-6">Kategorie aktywności</h1>
            <table className="w-full max-w-md bg-white rounded-lg shadow-md mt-8 mb-6">
                <thead>
                    <tr className="bg-indigo-200 text-gray-600 uppercase text-sm leading-normal">
                        <th className="py-3 px-6 ">Nazwa</th>
                        <th className="py-3 px-6">Opis</th>
                        <th className="py-3 px-6"></th>
                        <th className="py-3 px-6"></th>
                    </tr>
                </thead>
                <tbody className="text-gray-600 text-sm ">
                    {categories.map((category: any) => (
                        <tr key={category.id}>
                            <td className="py-3 px-6">{category.name}</td>
                            <td className="py-3 px-6">{category.description}</td>
                            <td className=" px-4 py-2">
                                <button onClick={() => handleDeleteCategory(category.id)}>
                                    <i className="fas fa-times text-indigo-400"></i>
                                </button>
                            </td>
                            <td className=" px-4 py-2">
                                <button onClick={() => showEditForm(category.id)}>
                                    <i className="fas fa-pencil-alt text-indigo-400"></i>
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <button onClick={() => showAddForm()}>
                <i
                    className="fas fa-plus-square text-indigo-400"
                    style={{ fontSize: "36px" }}
                ></i>
            </button>
            {addFormVisible && (
                <form
                    onSubmit={addFormik.handleSubmit}
                    className={formStyle}
                >
                    <h2 className="text-2xl font-bold mb-6">Dodaj kategorię</h2>
                    <div className="mb-4">
                        <label
                            htmlFor="name"
                            className={labelStyle}
                        >
                  Nazwa:
                        </label>

                        <input
                            type="text"
                            id="name"
                            name="name"
                            onChange={addFormik.handleChange}
                            className={inputStyle}
                        />
                        {addFormik.touched.name && addFormik.errors.name && (
                            <div className={errorStyle}>{addFormik.errors.name}</div>
                        )}
                    </div>

                    <div className="mb-4">
                        {" "}
                        <label
                            htmlFor="description"
                            className={labelStyle}
                        >
                  Opis:
                        </label>
                        <textarea
                            id="description"
                            name="description"
                            onChange={addFormik.handleChange}
                            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        ></textarea>
                        {addFormik.touched.description && addFormik.errors.description && (
                            <div className={errorStyle}>{addFormik.errors.description}</div>
                        )}
                    </div>

                    <button
                        type="submit"
                        className={buttonStyle}
                    >
                Utwórz kategorię
                    </button>
                </form>
            )}

            {editFormVisible && (
                <form
                    onSubmit={editFormik.handleSubmit}
                    className={formStyle}
                >
                    <h2 className="text-2xl font-bold mb-6">Edytuj kategorię</h2>
                    <div className="mb-4">
                        <label
                            htmlFor="name"
                            className={labelStyle}
                        >
                  Nazwa:
                        </label>

                        <input
                            type="text"
                            id="name"
                            name="name"
                            value={editFormik.values.name}
                            onChange={editFormik.handleChange}
                            className={inputStyle}
                        />
                        {editFormik.touched.name && editFormik.errors.name && (
                            <div className={errorStyle}>{editFormik.errors.name}</div>
                        )}
                    </div>

                    <div className="mb-4">
                        {" "}
                        <label
                            htmlFor="description"
                            className={labelStyle}
                        >
                  Opis:
                        </label>
                        <textarea
                            id="description"
                            name="description"
                            value={editFormik.values.description}
                            onChange={editFormik.handleChange}
                            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        ></textarea>
                        {editFormik.touched.description && editFormik.errors.description && (
                            <div className={errorStyle}>{editFormik.errors.description}</div>
                        )}
                    </div>

                    <button
                        type="submit"
                        className ={buttonStyle}
                    >
                Edytuj kategorię
                    </button>
                </form>
            )}
        </div>
    );
};
export default Categories;