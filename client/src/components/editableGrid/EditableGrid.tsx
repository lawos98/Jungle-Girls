import React, {useState, useRef, useEffect} from 'react';

type GridCellProps = {
    id: string;
    value: string;
    onChange: (value: string) => void;
    focusNextCell: (direction: 'up' | 'down' | 'left' | 'right') => void;
};

const GridCell: React.FC<GridCellProps> = ({id, value, onChange, focusNextCell}) => {
    const inputRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        if (inputRef.current) {
            inputRef.current.addEventListener('focus', () => {
                if (inputRef.current) {
                    inputRef.current.select();
                }
            });
        }
    }, []);

    const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
        switch (event.key) {
            case 'ArrowUp':
                focusNextCell('up');
                event.preventDefault();
                break;
            case 'ArrowDown':
                focusNextCell('down');
                event.preventDefault();
                break;
            case 'ArrowLeft':
                focusNextCell('left');
                event.preventDefault();
                break;
            case 'ArrowRight':
                focusNextCell('right');
                event.preventDefault();
                break;
        }
    };

    return (
        <input
            className={'border text-center border-gray-300 shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-indigo-500 w-full'}
            ref={inputRef}
            id={id}
            value={value}
            onChange={(e) => onChange(e.target.value)}
            onKeyDown={handleKeyDown}
        />
    );
};
const validateFloat = (value: string) => {
    const floatRegex = /^-?\d+(\.\d*)?$/;
    return value === '' || floatRegex.test(value);
};

const EditableGrid: React.FC = () => {
    const [zoomedRowIndex, setZoomedRowIndex] = useState<null | number>(null);
    const [zoomedColumnIndex, setZoomedColumnIndex] = useState<null | number>(null);
    const [isZoomedIn, setIsZoomedIn] = useState(false);


    //temp start
    const exampleData = [
        ['1.2', '3.4', '5.6', '7.8', '5'],
        ['7.8', '9.1', '2.3', '4.5', '5'],
        ['4.5', '6.7', '8.9', '1.2', '10'],
        ['1.2', '3.4', '5.6', '7.8', '5'],
        ['7.8', '9.1', '2.3', '4.5', '5'],
    ];

    const exampleColumnLabels = ['Column 1', 'Column 2', 'Column 3', 'Column 4', 'Column 5'];
    const exampleRowLabels = ['Row 1', 'Row 2', 'Row 3', 'Row 4', 'Row 5'];

    const [data, setData] = useState<string[][]>(exampleData);
    const [columnLabels, setColumnLabels] = useState<string[]>(exampleColumnLabels);
    const [rowLabels, setRowLabels] = useState<string[]>(exampleRowLabels);
    //temp end

    useEffect(() => {
        // TODO Fetch data from API and update state here
        // setData(fetchedData);
        // setColumnLabels(fetchedColumnLabels);
        // setRowLabels(fetchedRowLabels);
    }, []);
    const handleCellChange = (row: number, col: number, value: string) => {
        if (validateFloat(value)) {
            const newData = data.map((r, i) => r.map((c, j) => (i === row && j === col ? value : c)));
            setData(newData);
        }
    };

    const focusNextCell = (row: number, col: number, direction: 'up' | 'down' | 'left' | 'right') => {
        let newRow = row;
        let newCol = col;

        switch (direction) {
            case 'up':
                newRow = newRow - 1 >= 0 ? newRow - 1 : newRow;
                break;
            case 'down':
                newRow = newRow + 1 < data.length ? newRow + 1 : newRow;
                break;
            case 'left':
                newCol = newCol - 1 >= 0 ? newCol - 1 : newCol;
                break;
            case 'right':
                newCol = newCol + 1 < data[row].length ? newCol + 1 : newCol;
                break;
        }
        // if row is zoomed in, prevent moving to other rows
        if (zoomedRowIndex !== null) {
            newRow = zoomedRowIndex;
        }
        // if column is zoomed in, prevent moving to other columns
        if (zoomedColumnIndex !== null) {
            newCol = zoomedColumnIndex;
        }
        if (newRow !== row || newCol !== col) {
            const input = document.getElementById(`cell-${newRow}-${newCol}`) as HTMLInputElement;
            if (input) {
                input.focus();
                input.select();
            }
        }
    };

    return (
        <div
            className="flex flex-col w-min h-min transition-all duration-300 border"
            style={{
                transform: isZoomedIn ? 'scale(1.5)' : 'scale(1)',
                transformOrigin: 'top left',
            }}
            // onClick={handleBackgroundClick}
        >
            <div className="flex pb-1">
                <div className="w-20"></div>
                {columnLabels.map((label, index) => (
                    <div key={index}
                         className={`text-center whitespace-nowrap cursor-pointer transition-all duration-500 ease-in-out ${zoomedColumnIndex !== null && index !== zoomedColumnIndex ? 'opacity-0 max-w-px w-0 overflow-hidden' : 'opacity-100 max-w-24 w-24'} `}
                         onClick={() => {
                             setZoomedColumnIndex(zoomedColumnIndex === index ? null : index);
                             setIsZoomedIn(zoomedColumnIndex !== index);
                         }}
                    >
                        {label}
                    </div>
                ))}
            </div>
            {data.map((row, rowIndex) => {
                // if (zoomedRowIndex !== null && rowIndex !== zoomedRowIndex) return null;
                return (
                    <div key={`row-${rowIndex}`}
                         className={`flex items-center cursor-pointer transition-all duration-500 ease-in-out ${zoomedRowIndex !== null && rowIndex !== zoomedRowIndex ? 'opacity-0 max-h-0 overflow-hidden' : 'opacity-100 max-h-20'}  `}>
                        <div
                            className="text-center w-20  "
                            onClick={() => {
                                setZoomedRowIndex(zoomedRowIndex === rowIndex ? null : rowIndex);
                                setIsZoomedIn(zoomedRowIndex !== rowIndex);
                            }}
                        >
                            {rowLabels[rowIndex]}
                        </div>
                        {row.map((cell, colIndex) => (
                            // transition-all duration-500 ease-in-out ${zoomedColumnIndex !== null && colIndex !== zoomedColumnIndex ? 'opacity-0 max-w-0 max-h-0 overflow-hidden' : 'opacity-100 max-w-24 max-h-20'
                            <div key={`cell-${rowIndex}-${colIndex}`}
                                 className={`w-24 transition-all duration-500 ease-in-out ${zoomedColumnIndex !== null && colIndex !== zoomedColumnIndex ? 'opacity-0 max-w-px w-0 max-h-0 overflow-hidden' : 'opacity-100 max-w-24 max-h-20'}`}>
                                <GridCell
                                    id={`cell-${rowIndex}-${colIndex}`}
                                    value={cell}
                                    onChange={(value) => handleCellChange(rowIndex, colIndex, value)}
                                    focusNextCell={(direction) => focusNextCell(rowIndex, colIndex, direction)}
                                />
                            </div>
                        ))}
                    </div>
                );
            })}
        </div>
    );
};

export default EditableGrid
