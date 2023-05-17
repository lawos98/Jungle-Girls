import React, {useEffect, useState} from "react";
import {
    getStudentNotifications,
    StudentNotificationResponse,
    updateReadStudentNotification
} from "./MainStudentViewActions";
import Marquee from "react-fast-marquee";

const MainStudentView: React.FC = () => {
    const [notifications, setNotifications] = useState<StudentNotificationResponse[]>([]);

    useEffect(() => {
        (async () => {
            try {
                const data = await getStudentNotifications();
                setNotifications(data);
            } catch (error) {
                console.error("Error fetching student notifications:", error);
            }
        })();
    }, []);

    return (
        <div className="bg-gray-100 min-h-screen">
            <div className="container mx-auto p-8">
                <div className="mb-6 bg-blue-600 text-white py-2 px-4 rounded-md overflow-hidden">
                    <Marquee speed={30}>
                        JAKIS TAM EVENCIK SOBIE LECI
                    </Marquee>
                </div>
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                    <div className="bg-white rounded-lg shadow-md p-6">
                        <h1 className="text-2xl font-bold mb-6">Notyfikacje</h1>
                        <table className="w-full">
                            <thead>
                            <tr>
                                <th className="text-left">Data</th>
                                <th className="text-left">Aktywność</th>
                                <th className="text-left">Treść</th>
                                <th className="text-left">Autor</th>
                            </tr>
                            </thead>
                            <tbody>
                            {notifications.map((notification) => (
                                <tr
                                    key={notification.id}
                                    className={`${notification.wasRead ? "" : "font-bold"} border-t border-gray-200`}
                                    onMouseEnter={() => {
                                        if (!notification.wasRead) {
                                            notification.wasRead = true;
                                            setNotifications([...notifications]);
                                            updateReadStudentNotification(notification.id).then(response => console.log(response)).catch(error => {
                                                console.error("Error sending data to server:", error)
                                            });

                                        }
                                    }}
                                >
                                    <td className="py-2">{notification.date}</td>
                                    <td>{notification.subject}</td>
                                    <td>{notification.content}</td>
                                    <td>{notification.author}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                    <div className="bg-white rounded-lg shadow-md p-6">
                        <h1 className="text-2xl font-bold mb-6">Postęp studenta</h1>
                        {/* Tutaj można umieścić zawartość pokazującą postęp studenta */}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MainStudentView;
