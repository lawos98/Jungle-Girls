import React, { ReactNode } from "react";
import { Navigate } from "react-router-dom";
import Cookies from "js-cookie";
import {useSelector} from "react-redux";

interface PrivateRouteProps {
    children: ReactNode;
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({ children }) => {
    const auth = Cookies.get("token");
    const user = useSelector((state: any) => state.user);
    return (auth && user.isLogged) ? <>{children}</> : <Navigate to="/login" />;
};

export default PrivateRoute;
