import React from "react";
import { useEffect, useState } from "react";
import * as actions from "./CategoryActions";
import Cookies from "js-cookie";

const Categories: React.FC = () => {
  const [editedCategory, setEditedCategory] = useState({
    id: 0,
    name: "",
    description: "",
  });
  const [name, setName] = useState(editedCategory.name);
  const [description, setDescription] = useState(editedCategory.description);
  const [categories, setCategories] = useState([]);
  const [addFormVisible, setAddFormVisible] = useState(false);
  const [editFormVisible, setEditFormVisible] = useState(false);
  const [editedCategoryId, setEditedCategoryId] = useState<number>(0);

  const authToken = Cookies.get("token");

  useEffect(() => {
      actions.getCategories()
        .then((data) => setCategories(data));
  }, []);

  function handleDeleteCategory(id: number) {
    const payload = {
      id: id,
    };
    actions.deleteCategory(payload)
        .then(() => {
          setCategories(categories.filter((category: any) => category.id !== id));
        });
  }

  const handleEditCategory = async (e: React.FormEvent) => {
    e.preventDefault();

    const payload = {
      id: editedCategoryId,
      name: name,
      description: description,
    };

    if (!name.trim()) {
      alert("Nazwa kategorii nie może być pusta!");
      return;
    }

    if (!description.trim()) {
      alert("Opis kategorii nie może być pusty!");
      return;
    }

    actions.editCategory(payload)
        .then(() => {
          actions.getCategories()
              .then((response) => {
                setCategories(response);
                return response;
              });
        });

    closeEditForm();
  };

  const handleAddCategory = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!name.trim()) {
      alert("Nazwa kategorii nie może być pusta!");
      return;
    }

    if (!description.trim()) {
      alert("Opis kategorii nie może być pusty!");
      return;
    }

    closeAddForm();

    try {
      actions.createCategory(name, description, authToken).then(() => {
        actions.getCategories()
            .then((response) => {
              setCategories(response);
              return response;
            });
      });
    } catch (error) {
      console.error(error);
    }

    setName("");
    setDescription("");
  };

  function showAddForm(): void {
    closeEditForm();
    setAddFormVisible(true);
  }

  function closeAddForm(): void {
    setAddFormVisible(false);
  }

  function showEditForm(id: number): void {
    closeAddForm();
    let editedCategory = categories.filter((category) => category.id == id)[0];
    setEditedCategory(editedCategory);
    setDescription(editedCategory.description);
    setName(editedCategory.name);
    setEditFormVisible(true);
    setEditedCategoryId(id);
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
                onSubmit={handleAddCategory}
                className="bg-white rounded-lg shadow-md p-8 w-full max-w-md text-center"
            >
              <h2 className="text-2xl font-bold mb-6">Dodaj kategorię</h2>
              <div className="mb-4">
                <label
                    htmlFor="name"
                    className="block text-gray-700 font-medium text-center"
                >
                  Nazwa:
                </label>

                <input
                    type="text"
                    id="categoryName"
                    name="categoryName"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>

              <div className="mb-4">
                {" "}
                <label
                    htmlFor="description"
                    className="block text-gray-700 font-medium text-center"
                >
                  Opis:
                </label>
                <textarea
                    id="description"
                    name="description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                ></textarea>
              </div>

              <button
                  type="submit"
                  className="w-full bg-indigo-400 text-white py-2 px-4 rounded-md hover:bg-indigo-500 focus:outline-none focus:ring-4 focus:ring-indigo-500 focus:ring-opacity-50"
              >
                Utwórz kategorię
              </button>
            </form>
        )}

        {editFormVisible && (
            <form
                onSubmit={handleEditCategory}
                className="bg-white rounded-lg shadow-md p-8 w-full max-w-md text-center"
            >
              <h2 className="text-2xl font-bold mb-6">Edytuj kategorię</h2>
              <div className="mb-4">
                <label
                    htmlFor="name"
                    className="block text-gray-700 font-medium text-center"
                >
                  Nazwa:
                </label>

                <input
                    type="text"
                    id="categoryName"
                    name="categoryName"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>

              <div className="mb-4">
                {" "}
                <label
                    htmlFor="description"
                    className="block text-gray-700 font-medium text-center"
                >
                  Opis:
                </label>
                <textarea
                    id="description"
                    name="description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                ></textarea>
              </div>

              <button
                  type="submit"
                  className="w-full bg-indigo-400 text-white py-2 px-4 rounded-md hover:bg-indigo-500 focus:outline-none focus:ring-4 focus:ring-indigo-500 focus:ring-opacity-50"
              >
                Edytuj kategorię
              </button>
            </form>
        )}
      </div>
  );
};
export default Categories;