import React, {useEffect, useState} from "react";
import * as actions from "./LeaderboardActions";

type LeaderboardProps={
    groupId: number;
}
type Entry={
    rank: number;
    username: string;
    scoreSum: number|null;
}
const Leaderboard: React.FC<LeaderboardProps> = ({groupId}) => {
    const [isLoading, setIsLoading] = useState(true);
    const [entries, setEntries] = useState<Entry[]>([]);

    useEffect( () => {
        actions.getLeaderboard((data:any) => {
            console.log(data.scoreSumList);
            setEntries(data.scoreSumList)
            setIsLoading(false);
        });
    },[]);
    return (
        <div>
            {isLoading && <div>Loading...</div>}
            {entries.map((entry, index) => (
                <div key={index}>
                    {entry.rank} {entry.username} {entry.scoreSum}
                </div>
            ))}
        </div>
    );
}

export default Leaderboard;