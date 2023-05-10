import React from "react";
import {Link, useLocation, useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import Cookies from "js-cookie";
import {clearUser} from "../../reducers/UserReducer";

const Navbar: React.FC = () => {
    const user = useSelector((state: any) => state.user);
    const location = useLocation();
    const currentPath = location.pathname;
    const navigation = useNavigate();
    const dispatch = useDispatch();

    const isActive = (path: string) => currentPath === path;

    const handleLogout = () => {
        Cookies.remove("token");
        dispatch(clearUser());
        navigation("/login");
    };

    return (
        <nav className="bg-indigo-600 text-white p-4 mb-6">
            <div className="container mx-auto flex items-center justify-between">
                <div className="flex space-x-4">
                    <Link
                        to="/"
                        className={`${
                            isActive("/")
                                ? "text-indigo-100 font-semibold border-b-2 border-indigo-300"
                                : "hover:text-indigo-300"
                        }`}
                    >
                        Główny widok
                    </Link>
                    <Link
                        to="/grades"
                        className={`${
                            isActive("/grades")
                                ? "text-indigo-100 font-semibold border-b-2 border-indigo-300"
                                : "hover:text-indigo-300"
                        }`}
                    >
                        Oceny
                    </Link>
                    <Link
                        to="/leaderboard"
                        className={`${
                            isActive("/leaderboard")
                                ? "text-indigo-100 font-semibold border-b-2 border-indigo-300"
                                : "hover:text-indigo-300"
                        }`}
                    >
                        Leaderboard
                    </Link>
                </div>
                <div className="flex items-center space-x-4">
                    <Link
                        to="/account"
                        className={`${
                            isActive("/account")
                                ? "text-indigo-100 font-semibold border-b-2 border-indigo-300"
                                : "hover:text-indigo-300"
                        }`}
                    >
                        Konto
                    </Link>
                    <span className="font-semibold">{user.firstname} {user.lastname}</span>
                    <button
                        onClick={handleLogout}
                        className="bg-red-600 hover:bg-red-700 px-3 py-1 rounded"
                    >
                        Wyloguj
                    </button>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
