import React, { useEffect, useState } from "react";
import * as actions from "./StudentGradesActions";
import { ActivityScore } from "../types/EditableGridTypes";

interface GradesGridProps {
    // token: string;
}

const StudentGrades: React.FC<GradesGridProps> = () => {
    const [grades, setGrades] = useState<ActivityScore[]>([]);

    useEffect(() => {
        actions.getGrades((data: ActivityScore[]) => {
            console.log(data);
            setGrades(data);
        });
    }, []);

    return (
        <div className="w-full">
            <div className="grid grid-cols-[repeat(auto-fill,minmax(150px,1fr))] gap-4">
                {grades.map((grade, index) => (
                    <div
                        key={grade.activity.id}
                        className="border border-gray-300 rounded p-2 bg-white shadow"
                    >
                        <div className="text-sm font-semibold mb-1 text-gray-700">
                            {grade.activity.name}
                        </div>
                        <div className="flex justify-center items-center">
                            <div
                                className={`h-6 ${
                                    grade.values !== null ? "text-green-500" : "text-red-500"
                                }`}
                            >
                                {grade.values !== null ? (
                                    <span>
                                        {grade.values}/{grade.activity.maxScore}
                                    </span>
                                ) : (
                                    <span>-</span>
                                )}
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default StudentGrades;
