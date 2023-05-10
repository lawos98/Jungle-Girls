import React, {useEffect, useRef} from "react";

type GridCellProps = {
    isZoomedIn: boolean;
    id: string;
    value: string;
    onChange: (value: string) => void;
    focusNextCell: (direction: "up" | "down" | "left" | "right") => void;
};

const GridCell: React.FC<GridCellProps> = React.memo(({isZoomedIn, id, value, onChange, focusNextCell}) => {
    const inputRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        if (inputRef.current) {
            inputRef.current.addEventListener("focus", () => {
                if (inputRef.current) {
                    inputRef.current.select();
                }
            });
        }
    }, []);

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (["ArrowUp", "ArrowDown", "ArrowLeft", "ArrowRight"].includes(e.key)) {
            e.preventDefault();
            const direction = e.key.replace("Arrow", "").toLowerCase() as
                | "up"
                | "down"
                | "left"
                | "right";
            focusNextCell(direction);
        }
    };

    return (
        <input
            className={`border-collapse border text-center transition-all duration-500 ease-in-out ${isZoomedIn? "border-blue-400": "border-gray-300"} py-2 px-3 text-gray-700 focus:outline-none focus:z-10  w-full`}
            ref={inputRef}
            id={id}
            value={value}
            onChange={(e) => onChange(e.target.value)}
            onKeyDown={handleKeyDown}
        />
    );
});

export default GridCell;