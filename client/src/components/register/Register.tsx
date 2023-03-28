import React, { useState } from 'react';
import * as actions from './RegisterActions';

const Register: React.FC = () => {
    const [email, setEmail] = useState('');
    const [studentIndex, setStudentIndex] = useState('');
    const [repositoryLink, setRepositoryLink] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        actions.register(email, studentIndex, repositoryLink, password);
        console.log('Email:', email, 'Student Index:', studentIndex, 'Repository Link:', repositoryLink, 'Password:', password, 'Confirm Password:', confirmPassword);
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
                        htmlFor="email"
                        className="block text-gray-700 font-medium text-center"
                    >
                        Email
                    </label>
                    <input
                        id="email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                </div>
                <div className="mb-4">
                    <label
                        htmlFor="studentIndex"
                        className="block text-gray-700 font-medium text-center"
                    >
                        Index studenta
                    </label>
                    <input
                        id="studentIndex"
                        type="text"
                        value={studentIndex}
                        onChange={(e) => setStudentIndex(e.target.value)}
                        className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                </div>
                <div className="mb-4">
                    <label
                        htmlFor="repositoryLink"
                        className="block text-gray-700 font-medium text-center"
                    >
                        Link do repozytorium
                    </label>
                    <input
                        id="repositoryLink"
                        type="text"
                        value={repositoryLink}
                        onChange={(e) => setRepositoryLink(e.target.value)}
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

