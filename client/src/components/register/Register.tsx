import React, { useState } from "react";
import * as actions from "./RegisterActions";

const Register: React.FC = () => {
    const [username, setUsername] = useState("");
    const [firstname, setFirstName] = useState("");
    const [lastname, setLastName] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            alert("Hasła nie są takie same!");
            return;
        }
        actions.register(username, firstname, lastname, password);
    };

    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center">
            <form
                onSubmit={handleSubmit}
                className="bg-white rounded-lg shadow-md p-8 w-full max-w-md text-center"
            >
                <h2 className="text-2xl font-bold mb-6">Rejestracja</h2>
                <div className="mb-4">
                    <label
                        htmlFor="username"
                        className="block text-gray-700 font-medium text-center"
                    >
                        Nazwa użytkownika
                    </label>
                    <input
                        id="username"
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                </div>
                <div className="mb-4">
                    <label
                        htmlFor="firstname"
                        className="block text-gray-700 font-medium text-center"
                    >
                        Imię
                    </label>
                    <input
                        id="firstname"
                        type="text"
                        value={firstname}
                        onChange={(e) => setFirstName(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                </div>
                <div className="mb-4">
                    <label
                        htmlFor="lastname"
                        className="block text-gray-700 font-medium text-center"
                    >
                        Nazwisko
                    </label>
                    <input
                        id="lastname"
                        type="text"
                        value={lastname}
                        onChange={(e) => setLastName(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                </div>
                <div className="mb-4">
                    <label
                        htmlFor="password"
                        className="block text-gray-700 font-medium text-center"
                    >
                        Hasło
                    </label>
                    <input
                        id="password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:            ring-indigo-500"
                    />
                </div>
                <div className="mb-6">
                    <label
                        htmlFor="confirmPassword"
                        className="block text-gray-700 font-medium text-center"
                    >
                        Powtórz hasło
                    </label>
                    <input
                        id="confirmPassword"
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                </div>
                <button
                    type="submit"
                    className="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-4 focus:ring-indigo-500 focus:ring-opacity-50"
                >
                    Zarejestruj się
                </button>
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
