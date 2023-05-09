import React from "react";
const EditableGridSkeleton: React.FC = () => {
    return (
        <div className="max-w-7xl w-full">
            <div>
                <div className="flex pb-2">
                    <div className="w-60"></div>
                    {Array(12) // Change the number of columns if needed
                        .fill(null)
                        .map((_, index) => (
                            <div
                                key={index}
                                className="rounded-full bg-gray-300 animate-pulse h-4 w-32 mr-1"
                            ></div>
                        ))}
                </div>
                {Array(12) // Change the number of rows if needed
                    .fill(null)
                    .map((_, rowIndex) => (
                        <div key={`row-${rowIndex}`} className="flex items-center ">
                            <div className="text-center w-60 rounded-full bg-gray-300 h-4 animate-pulse mr-2"></div>
                            {Array(12) // Change the number of cells in each row if needed
                                .fill(null)
                                .map((_, colIndex) => (
                                    <div
                                        key={`cell-${rowIndex}-${colIndex}`}
                                        className="border border-gray-300 animate-pulse h-12 w-32"
                                    ></div>
                                ))}
                        </div>
                    ))}
            </div>
        </div>
    );
};
export default EditableGridSkeleton;