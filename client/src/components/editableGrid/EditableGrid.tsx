import React, {useState, useRef, useEffect, useCallback} from 'react';
import Swiper from "swiper";
import "swiper/css/bundle";
import * as actions from "./EditableGridActions"
import { ActivityScoreList } from '../types/EditableGridTypes';
import EditableGridSkeleton from "./EditableGridSkeleton";
import toast from 'react-hot-toast';
import GridCell from "./GridCell";
import validateInput from "../../utils/utils"
type EditableGridProps = {
    groupId: number;
}
// TODO: Add search functionality (maybe another task)
// TODO: add pagination (another task)
const EditableGrid: React.FC<EditableGridProps> = ({groupId}) => {
    const [zoomedRowIndex, setZoomedRowIndex] = useState<null | number>(null);
    const [zoomedColumnIndex, setZoomedColumnIndex] = useState<null | number>(null);
    const [isSliding, setIsSliding] = useState(false);
    const [swiper, setSwiper] = useState<any>(null);
    const [isLoading, setIsLoading] = useState(true);


    const containerRef = useRef<HTMLDivElement>(null);
    const swiperSetupFlag = useRef(false);

    const [data, setData] = useState<string[][]>([[]]);
    const [columnLabels, setColumnLabels] = useState<string[]>([]);
    const [rowLabels, setRowLabels] = useState<string[]>([]);

    const [scoreData, setScoreData] = useState<ActivityScoreList[]>([]);
    const [changedCells, setChangedCells] = useState<{ [key: string]: string }>({});

    useEffect(() => {
        actions.getGrades(groupId,(scoreData:any) => {
            setScoreData(scoreData);
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
    const handleCellChange = useCallback((row: number, col: number, value: string) => {
        if (validateInput(value, 0, scoreData[col].activity.maxScore)) {
            const newData = data.map((r, i) => r.map((c, j) => (i === row && j === col ? value : c)));
            setData(newData);
            setChangedCells({ ...changedCells, [`${row}-${col}`]: value });
        }
        else{
            toast.error(`Wartość musi być między 0 a ${scoreData[col].activity.maxScore}`);
        }
    },[data,scoreData]);
    const handleSaveGrades = () => {
        const updatedActivityScoreList: ActivityScoreList[] = [];

        Object.keys(changedCells).forEach((key) => {
            const [rowIndex, colIndex] = key.split('-').map(Number);
            const activity = scoreData[colIndex].activity;
            const student = scoreData[colIndex].students[rowIndex];
            const updatedValue = parseFloat(changedCells[key]);

            let activityScoreList = updatedActivityScoreList.find(
                (item) => item.activity.id === activity.id
            );

            if (!activityScoreList) {
                activityScoreList = {
                    activity,
                    students: [],
                };
                updatedActivityScoreList.push(activityScoreList);
            }
            console.log(activity, student, updatedValue);
            const studentScore = {
                ...student,
                value: isNaN(updatedValue) ? null : updatedValue,
            };
            activityScoreList.students.push(studentScore);
        });

        const updateGradesPromise = actions.updateGrades(groupId, updatedActivityScoreList);
        toast.promise(updateGradesPromise, {
            loading: 'Zapisuję Oceny...',
            success: 'Oceny Zapisane!',
            error: 'Błąd podczas zapisywania ocen',
        });
    };

    const handleBackgroundClick = (e: MouseEvent) => {
        if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
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
        >
            {isLoading && (
                <EditableGridSkeleton></EditableGridSkeleton>
            )}

            <div
                className="border-collapse flex flex-col h-min transition-all duration-300"
            >
                <div className="flex pb-1">
                    <div className="w-40"></div>
                    {columnLabels.map((label, index) => (
                        // activities names
                        <div key={index}
                             className={`w-24 border-collapse text-center whitespace-nowrap cursor-pointer transition-all duration-500 ease-in-out ${zoomedColumnIndex !== null && index !== zoomedColumnIndex ? "opacity-50 overflow-hidden" : "opacity-100"} ${zoomedColumnIndex !== null && index == zoomedColumnIndex ? "origin-top scale-105" : "origin-left scale-100"}`}
                             onClick={() => {
                                 // if the row is zoomed in, zoom out and zoom in the column
                                 if (zoomedRowIndex !== null) {
                                     setZoomedRowIndex(null);
                                     setTimeout(() => {
                                         setZoomedColumnIndex(zoomedColumnIndex === index ? null : index);
                                     }, 200);
                                 }
                                 else {
                                     setZoomedColumnIndex(zoomedColumnIndex === index ? null : index);
                                 }
                             }}
                        >
                            {label}
                            <p>0-{scoreData[index].activity.maxScore}</p>
                        </div>
                    ))}
                </div>
                {data.map((row, rowIndex) => {
                    // if (zoomedRowIndex !== null && rowIndex !== zoomedRowIndex) return null;
                    return (
                        <div key={`row-${rowIndex}`}
                             className={`border-collapse flex items-center cursor-pointer transition-all duration-500 ease-in-out ${zoomedRowIndex !== null && rowIndex !== zoomedRowIndex ? "opacity-50 max-h-20 overflow-hidden" : "opacity-100 max-h-20"} ${zoomedRowIndex !== null && rowIndex == zoomedRowIndex ? "origin-top scale-[1.03] z-10" : "origin-left scale-100 z-0"}`}>
                            <div
                                // names
                                className="text-center w-40"
                                onClick={() => {
                                    // if the column is zoomed in, zoom out and zoom in the row
                                    if (zoomedColumnIndex !== null) {
                                        setZoomedColumnIndex(null);
                                        setTimeout(() => {
                                            setZoomedRowIndex(zoomedRowIndex === rowIndex ? null : rowIndex);
                                        }, 200);
                                        }
                                    else {
                                    setZoomedRowIndex(zoomedRowIndex === rowIndex ? null : rowIndex);
                                    }
                                    // slide to the row
                                    swiper.slideTo(rowIndex, 10);
                                }}
                            >
                                {rowLabels[rowIndex]}
                            </div>
                            {row.map((cell, colIndex) => (
                                // transition-all duration-500 ease-in-out ${zoomedColumnIndex !== null && colIndex !== zoomedColumnIndex ? 'opacity-0 max-w-0 max-h-0 overflow-hidden' : 'opacity-100 max-w-24 max-h-20'
                                <div key={`cell-${rowIndex}-${colIndex}`}
                                     className={`w-24 border-collapse transition-all duration-500 ease-in-out ${zoomedColumnIndex !== null && colIndex !== zoomedColumnIndex ? "opacity-50 overflow-hidden" : "opacity-100"} ${zoomedColumnIndex !== null && colIndex == zoomedColumnIndex ? "origin-top scale-110 z-10" : "origin-bottom scale-100 z-0"}`}>
                                    <GridCell
                                        isZoomedIn={rowIndex === zoomedRowIndex || colIndex === zoomedColumnIndex}
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
            <div
                className={`ml-[410px] mt-5 slide-in ${zoomedRowIndex !== null ? "slide-in-y-up" : "slide-out-y-up"}`}>
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
            <div className={'mt-5 flex justify-center'}>
                {!isLoading && (
                    <button onClick={handleSaveGrades} className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                        Save Grades
                    </button>)
                }
            </div>
        </div>
    );
};

export default EditableGrid
