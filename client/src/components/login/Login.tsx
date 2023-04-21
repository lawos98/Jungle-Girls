import React, { useState } from "react";
import * as actions from "./LoginActions";

const Login: React.FC = () => {
    const [nick, setNick] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        actions.login(nick, password);
    };

    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center">
            <form
                onSubmit={handleSubmit}
                className="bg-white rounded-lg shadow-md p-8 w-full max-w-md text-center"
            >
                <h2 className="text-2xl font-bold mb-6">Logowanie</h2>
                <div className="mb-4">
                    <label
                        htmlFor="nick"
                        className="block text-gray-700 font-medium text-center">
                        Nick :)
                    </label>
                    <input
                        id="nick"
                        type="text"
                        value={nick}
                        onChange={(e) => setNick(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                </div>
                <div className="mb-6">
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
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                </div>
                <button
                    type="submit"
                    className="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-4 focus:ring-indigo-500 focus:ring-opacity-50 text-center"
                >
                    Zaloguj się
                </button>
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

