import React, {useEffect, useRef, useState} from "react";
import {
    EventResponse,
    getEvents,
    getScore,
    getStudentNotifications,
    ScoreResponse,
    StudentNotificationResponse,
    updateReadStudentNotification,
} from "./MainStudentViewActions";
import zombie0 from "./img/zombie0.png";
import zombie1 from "./img/zombie1.png";
import zombie2 from "./img/zombie2.png";
import zombie3 from "./img/zombie3.png";
import Marquee from "react-fast-marquee";
import {DateTime, Duration, Interval} from "luxon";

const MainStudentView: React.FC = () => {
    const [notifications, setNotifications] = useState<StudentNotificationResponse[]>([]);
    const [score, setScore] = useState<ScoreResponse>();
    const [events, setEvents] = useState<EventResponse[]>([]);
    const [remainingTimes, setRemainingTimes] = useState<{ [eventName: string]: string }>({});

    useEffect(() => {
        const intervalId = setInterval(() => {
            const newRemainingTimes = {};
            for (const event of events) {
                // @ts-ignore
                newRemainingTimes[event.name] = calculateRemainingTime(event.startDate, event.duration);
            }
            setRemainingTimes(newRemainingTimes);
        }, 1000);

        return () => {
            clearInterval(intervalId);
        };
    }, [events]);

    const calculateRemainingTime = (date?: string, duration?: string) => {
        if (!date || !duration) return '';
        const durationInHours = Duration.fromISO(duration).shiftTo('hours').hours;
        const startDate = DateTime.fromISO(date);
        const endDate = startDate.plus({hours: durationInHours});
        const now = DateTime.local();

        if (now > endDate) {
            return 'Wydarzenie się zakończyło.';
        }

        const remainingInterval = Interval.fromDateTimes(now, endDate);
        const remainingDuration = remainingInterval.toDuration(['days','hours', 'minutes', 'seconds']);
        return `Koniec za ${Math.floor(remainingDuration.days)}d ${Math.floor(remainingDuration.hours)}h ${remainingDuration.minutes}m ${Math.round(remainingDuration.seconds)}s`;
    }

    // TODO Remove - only for testing
    const addPoint = useRef(() => {
    });
    const subtractPoint = useRef(() => {
    });

    addPoint.current = () => {
        if (score) {
            setScore({...score, points: Math.min(score.points + 1, score.maxPoints)});
        }
    };

    subtractPoint.current = () => {
        if (score) {
            setScore({...score, points: Math.max(score.points - 1, 0)});
        }
    };
    //

    useEffect(() => {
        (async () => {
            try {
                const data = await getStudentNotifications();
                setNotifications(data);
            } catch (error) {
                console.error("Error fetching student notifications:", error);
            }
        })();

        getScore((data: any) => {
            setScore(data);
            console.log(data);
        });

        getEvents((data: any) => {
            setEvents(data);
            console.log(data);
        });

        //TODO: Remove - only for testing
        const handleKeyDown = (event: KeyboardEvent) => {
            switch (event.key) {
                case ",":
                    subtractPoint.current();
                    break;
                case ".":
                    addPoint.current();
                    break;
                default:
                    break;
            }
        };

        window.addEventListener("keydown", handleKeyDown);
        return () => window.removeEventListener("keydown", handleKeyDown);
        //
    }, []);
    const getStageImage = () => {
        if (!score) return zombie0;
        const percentage = score.points / score.maxPoints;
        if (percentage < 0.25) return zombie0;
        if (percentage < 0.5) return zombie1;
        if (percentage < 0.75) return zombie2;
        return zombie3;
    };

    const calculateDate = (date?: string, duration?: string) => {
        if (!date || !duration) return '';
        const durationInHours = Duration.fromISO(duration).shiftTo('hours').hours;
        const startDate = DateTime.fromISO(date);
        const endDate = startDate.plus({hours: durationInHours});

        return endDate.toFormat('dd.MM.yyyy HH:mm')

    }


    return (
        <div className="bg-gray-100 min-h-screen">
            <div className="container mx-auto p-8">
                <div className="flex flex-col">
                    {events.map((currentEvent) => (
                        <div className="mb-6 bg-blue-600 text-white py-2 px-4 rounded-md overflow-hidden flex">
                            <Marquee speed={50}>
                                {currentEvent?.name + " " + calculateDate(currentEvent?.startDate, currentEvent?.duration)}
                            </Marquee>
                            <div className="p-1 whitespace-nowrap border-l-2">{remainingTimes[currentEvent?.name]}</div>
                        </div>
                    ))}
                </div>
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                    <div className="bg-white rounded-lg shadow-md p-6">
                        <h1 className="text-2xl font-bold mb-6">Notyfikacje</h1>
                        <table className="w-full">
                            <thead>
                            <tr>
                                <th className="text-left">Data</th>
                                <th className="text-left">Temat</th>
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
                        <img src={getStageImage()} alt="Zombie" className="mx-auto mb-4"/>
                        <div className="w-full h-6 bg-gray-200 rounded-full overflow-hidden">
                            <div
                                className="h-full bg-red-500"
                                style={{width: `${(score?.points || 0) / (score?.maxPoints || 1) * 100}%`}}
                            />
                        </div>
                        <div className="text-center mt-2">
                            {score?.points} / {score?.maxPoints}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default MainStudentView;
