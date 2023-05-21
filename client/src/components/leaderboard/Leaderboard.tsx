import React, {useEffect, useState} from "react";
import * as actions from "./LeaderboardActions";


type Entry = {
    rank: number;
    username: string;
    scoreSum: number | null;
}
const Leaderboard: React.FC = () => {
    const [entries, setEntries] = useState<Entry[]>([]);
    const [username, setUsername] = useState<string>("");


    useEffect(() => {
        actions.getLeaderboard((data: any) => {
            console.log(data.scoreSumList);
            setEntries(data.scoreSumList)
            setUsername(data.username)

        });
    }, []);




    return (
        <div className="flex flex-col items-center w-full border ">
            {/*{isLoading && <div>Loading...</div>}*/}
            <div className="w-2/3 bg-white rounded-lg shadow-md p-4 mb-4 flex items-center">
                <div className="w-1/6  font-bold text-2xl text-center">
                    Miejsce
                </div>
                <div className="w-2/3 text-center font-bold text-2xl">
                    Nick
                </div>
                <div className="w-1/6  font-bold text-2xl text-center">
                    Punkty
                </div>
            </div>
            {entries.map((entry, index) => (
                <div key={index}
                     className={`w-2/3 bg-white rounded-lg shadow-md p-4 mb-4 flex items-center slide-in-y-up ${username === entry.username ? "shadow-indigo-600 text-blue-800" : ""}`}>
                    <div className="w-1/6 text-blue-500 font-bold text-xl text-center">
                        {entry.rank}.
                    </div>
                    <div className={`w-2/3 text-center text-xl font-semibold`}>
                        {entry.username}
                    </div>
                    <div className="w-1/6 text-blue-500 font-bold text-xl text-center">
                        {entry.scoreSum}
                    </div>
                </div>
            ))}
        </div>
    );
}

export default Leaderboard;