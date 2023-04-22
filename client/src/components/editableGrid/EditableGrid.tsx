import React, {useState, useRef, useEffect} from 'react';
import Swiper from "swiper";
import "swiper/css/bundle";
import * as actions from "./EditableGridActions"

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
    const [isSliding, setIsSliding] = useState(false);
    const [swiper, setSwiper] = useState<any>(null);
    const [isLoading, setIsLoading] = useState(true);


    const containerRef = useRef<HTMLDivElement>(null);
    const swiperSetupFlag = useRef(false);

    const [data, setData] = useState<string[][]>([[]]);
    const [columnLabels, setColumnLabels] = useState<string[]>([]);
    const [rowLabels, setRowLabels] = useState<string[]>([]);

    useEffect(() => {
        actions.getGrades(1,(scoreData:any) => {
            console.log(scoreData);
            setColumnLabels(scoreData.map((item:any) => item.activity.name));
            setRowLabels(scoreData[0].students.map((item:any) => item.firstname + " " + item.lastname));
            const organizedData: string[][] = scoreData[0].students.map((_: any, rowIndex: number) => {
                return scoreData.map((item: any) => {
                    const student = item.students[rowIndex];
                    return student.value === null ? "" : student.value.toString();
                });
            });
            setData(organizedData);
            setIsLoading(false);
        })
        setupSwiper();

        document.addEventListener("click", handleBackgroundClick);
        // Remove the event listener when the component is unmounted
        return () => {
            document.removeEventListener("click", handleBackgroundClick);
        };
    }, []);

    const setupSwiper = () => {
        if (!swiperSetupFlag.current) {
            const swiperInstance = (new Swiper('.swiper', {
                slidesPerView: 3,
                spaceBetween: 10,
                centeredSlides: true,
                grabCursor: false,
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                }
            }));
            // remove any existing event listeners
            swiperInstance.off('slideChange');
            swiperInstance.on('slideChange', () => {
                setZoomedRowIndex(swiperInstance.activeIndex);
            });
            // set isSliding to true when the slide transition starts
            swiperInstance.off('transitionStart');
            swiperInstance.on('transitionStart', () => {
                setIsSliding(true);
            });
            // set isSliding to false when the slide transition ends
            swiperInstance.off('transitionEnd');
            swiperInstance.on('transitionEnd', () => {
                setIsSliding(false);
            });
            setSwiper(swiperInstance);
            swiperSetupFlag.current = true;

        }
    }
    const handleCellChange = (row: number, col: number, value: string) => {
        if (validateFloat(value)) {
            const newData = data.map((r, i) => r.map((c, j) => (i === row && j === col ? value : c)));
            setData(newData);
        }
    };
    const handleBackgroundClick = (e: MouseEvent) => {
        if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
            setIsZoomedIn(false);
            setZoomedRowIndex(null);
            setZoomedColumnIndex(null);
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
            ref={containerRef}
            className={"min-w-0"}
            // onClick={handleBackgroundClick}
        >
            {isLoading && (
                <div className="max-w-7xl w-full">
                    <div>
                        <div className="flex pb-2">
                            <div className="w-60"></div>
                            {Array(5) // Change the number of columns if needed
                                .fill(null)
                                .map((_, index) => (
                                    <div
                                        key={index}
                                        className="rounded-full bg-gray-300 animate-pulse h-4 w-32 mr-1"
                                    ></div>
                                ))}
                        </div>
                        {Array(5) // Change the number of rows if needed
                            .fill(null)
                            .map((_, rowIndex) => (
                                <div key={`row-${rowIndex}`} className="flex items-center mb-2">
                                    <div className="text-center w-60 rounded-full bg-gray-300 h-4 animate-pulse mr-2"></div>
                                    {Array(5) // Change the number of cells in each row if needed
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
            )}



            <div
                className="flex flex-col h-min transition-all duration-300  min-w-0"
                style={{
                    transform: isZoomedIn ? 'scale(1.5)' : 'scale(1)',
                    transformOrigin: 'top left',
                }}
                // onClick={handleBackgroundClick}
            >
                <div className="flex pb-1">
                    <div className="w-40"></div>
                    {columnLabels.map((label, index) => (
                        <div key={index}
                             className={`text-center whitespace-nowrap cursor-pointer transition-all duration-500 ease-in-out ${zoomedColumnIndex !== null && index !== zoomedColumnIndex ? 'opacity-0 max-w-px w-0 overflow-hidden' : 'opacity-100 max-w-24 w-24'} `}
                             onClick={() => {
                                 // if the row is zoomed in, zoom out and zoom in the column
                                 if (zoomedRowIndex !== null) {
                                     setZoomedRowIndex(null);
                                     setIsZoomedIn(false);
                                     setTimeout(() => {
                                         setZoomedColumnIndex(zoomedColumnIndex === index ? null : index);
                                         setIsZoomedIn(zoomedColumnIndex !== index);
                                     }, 300);
                                 }
                                 else {
                                     setZoomedColumnIndex(zoomedColumnIndex === index ? null : index);
                                     setIsZoomedIn(zoomedColumnIndex !== index);
                                 }
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
                             className={`flex items-center cursor-pointer ${!isSliding ? 'transition-all duration-500 ease-in-out':''}  ${zoomedRowIndex !== null && rowIndex !== zoomedRowIndex ? 'opacity-0 max-h-0 overflow-hidden' : 'opacity-100 max-h-20'}  `}>
                            <div
                                className="text-center w-40  "
                                onClick={() => {
                                    // if the column is zoomed in, zoom out and zoom in the row
                                    if (zoomedColumnIndex !== null) {
                                        setZoomedColumnIndex(null);
                                        setIsZoomedIn(false);
                                        setTimeout(() => {
                                            setZoomedRowIndex(zoomedRowIndex === rowIndex ? null : rowIndex);
                                            setIsZoomedIn(zoomedRowIndex !== rowIndex);
                                        }, 500);
                                        }
                                    else {
                                    setZoomedRowIndex(zoomedRowIndex === rowIndex ? null : rowIndex);
                                    setIsZoomedIn(zoomedRowIndex !== rowIndex);
                                    }
                                    // immidiately slide to the row
                                    swiper.slideTo(rowIndex, 0);
                                }}
                            >
                                {rowLabels[rowIndex]}
                            </div>
                            {row.map((cell, colIndex) => (
                                // transition-all duration-500 ease-in-out ${zoomedColumnIndex !== null && colIndex !== zoomedColumnIndex ? 'opacity-0 max-w-0 max-h-0 overflow-hidden' : 'opacity-100 max-w-24 max-h-20'
                                <div key={`cell-${rowIndex}-${colIndex}`}
                                     className={`transition-all duration-500 ease-in-out ${zoomedColumnIndex !== null && colIndex !== zoomedColumnIndex ? 'opacity-0 max-w-px w-px max-h-0 overflow-hidden' : 'opacity-100 max-w-24 w-24 max-h-20'}`}>
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
                {

                    <div
                        className={`scale-100 flex  mt-5 ${zoomedRowIndex !== null ? "visible" : "hidden"}`}>

                        <div className="swiper">
                            <div className="swiper-wrapper">
                                {rowLabels.map((label, index) => (
                                    <div key={index} className="swiper-slide">{label}</div>
                                ))}
                            </div>

                            <div className="swiper-button-prev"></div>
                            <div className="swiper-button-next"></div>
                        </div>
                    </div>

                }
            </div>
        </div>

    );
};

export default EditableGrid
