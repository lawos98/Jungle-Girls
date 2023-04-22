import React, { ReactNode } from "react";
import { Navigate } from "react-router-dom";
import Cookies from "js-cookie";

interface PrivateRouteProps {
    children: ReactNode;
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({ children }) => {
    const auth = Cookies.get("token");
    return auth ? <>{children}</> : <Navigate to="/login" />;
};

export default PrivateRoute;
