import React, {useState, useRef, useEffect, useCallback} from 'react';
import Swiper from "swiper";
import "swiper/css/bundle";
import * as actions from "./EditableGridActions"
import {Activity, ActivityScoreList} from '../types/EditableGridTypes';
import EditableGridSkeleton from "./EditableGridSkeleton";
import toast from 'react-hot-toast';
import GridCell from "./GridCell";
import validateInput from "../../utils/utils"
type EditableGridProps = {
    groupId: number;
}
type Grade = {
    score: string;
    activityId: number;
    activityCategoryId: number;
    maxScore: number;
    activity: Activity;
}
// TODO: add colors :)
const EditableGrid: React.FC<EditableGridProps> = ({groupId}) => {
    const [zoomedRowIndex, setZoomedRowIndex] = useState<null | number>(null);
    const [zoomedColumnIndex, setZoomedColumnIndex] = useState<null | number>(null);
    const [isSliding, setIsSliding] = useState(false);
    const [swiper, setSwiper] = useState<any>(null);
    const [isLoading, setIsLoading] = useState(true);


    const containerRef = useRef<HTMLDivElement>(null);
    const swiperSetupFlag = useRef(false);

    const [data, setData] = useState<Grade[][]>([[]]);
    const [columnLabels, setColumnLabels] = useState<string[]>([]);
    const [rowLabels, setRowLabels] = useState<string[]>([]);

    const [scoreData, setScoreData] = useState<ActivityScoreList[]>([]);
    const [changedCells, setChangedCells] = useState<{ [key: string]: string }>({});
    const [categories, setCategories] = useState<Array<{ id: number; name: string }>>([]);
    const [foldedCategories, setFoldedCategories] = useState<{ [key: number]: boolean }>({});


    useEffect(() => {
        actions.getGrades(groupId, (scoreData: any) => {
            setScoreData(scoreData);
            console.log(scoreData);
            setRowLabels(
                scoreData[0].students.map(
                    (item: any) => item.firstname + " " + item.lastname
                )
            );

            let activityCategoryIds: number[];
            activityCategoryIds = Array.from(
                new Set(scoreData.map((item: any) => item.activity.activityCategoryId))
            );
            console.log(activityCategoryIds);

            actions.getCategories(activityCategoryIds, (categories: Array<{ id: number; name: string }>) => {
                // const uniqueCategories = categories.map((item: any) => item.name);
                // console.log(uniqueCategories);
                setCategories(categories);

                const initialFoldedCategories = categories.reduce(
                    (acc: { [key: number]: boolean }, category: { id: number; name: string }) => {
                        acc[category.id] = false;
                        return acc;
                    },
                    {}
                );
                setFoldedCategories(initialFoldedCategories);

                const groupedActivities = categories.reduce(
                    (acc: { [key: number]: any[] }, category: any) => {
                        acc[category.id] = scoreData.filter(
                            (item: any) => item.activity.activityCategoryId === category.id
                        );
                        return acc;
                    },
                    {}
                );

                const columnLabels = [];
                for (const categoryId of activityCategoryIds) {
                    const activities = groupedActivities[categoryId];
                    for (const activity of activities) {
                        columnLabels.push(activity.activity.name);
                    }
                }
                setColumnLabels(columnLabels);

                const organizedData: Grade[][] = scoreData[0].students.map(
                    (_: any, rowIndex: number) => {
                        const rowData = [];
                        for (const categoryId of activityCategoryIds) {
                            const activities = groupedActivities[categoryId];
                            for (const activity of activities) {
                                const student = activity.students[rowIndex];
                                const score = student.value === null ? "" : student.value.toString();
                                const activityId = activity.activity.id;
                                const activityCategoryId = activity.activity.activityCategoryId;
                                const maxScore = activity.activity.maxScore;
                                rowData.push({ score, activityId, activityCategoryId, maxScore});
                            }
                        }
                        return rowData;
                    }
                );
                console.log(organizedData)
                setData(organizedData);
                setIsLoading(false);
            });
        });
        setupSwiper();

    document.addEventListener("click", handleBackgroundClick);
        // Remove the event listener when the component is unmounted
        return () => {
            document.removeEventListener("click", handleBackgroundClick);
        };
    }, []);
    const toggleFoldCategory = (categoryId: number) => {
        setFoldedCategories({ ...foldedCategories, [categoryId]: !foldedCategories[categoryId] });
    };

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
    const handleCellChange = useCallback(
        (row: number, col: number, value: string) => {
            if (
                validateInput(value, 0, data[row][col].maxScore)
            ) {
                const newData = data.map((r, i) =>
                    r.map((c, j) =>
                        i === row && j === col
                            ? { ...c, score: value }
                            : c
                    )
                );
                setData(newData);
                setChangedCells({ ...changedCells, [`${row}-${col}`]: value });
            } else {
                toast.error(
                    `Wartość musi być między 0 a ${data[row][col].maxScore}`
                );
            }
        },
        [data, scoreData]
    );

    const handleSaveGrades = () => {
        const updatedActivityScoreList: ActivityScoreList[] = [];
        Object.keys(changedCells).forEach((key) => {
            const [rowIndex, colIndex] = key.split('-').map(Number);
            const activityId = data[rowIndex][colIndex].activityId;
            const student = scoreData[colIndex].students[rowIndex];
            const updatedValue = parseFloat(changedCells[key]);

            let activityScoreList = updatedActivityScoreList.find(
                (item) => item.activity.id === activityId
            );
            if (!activityScoreList) {
                activityScoreList = scoreData.find((item) => item.activity.id === activityId);
                if (activityScoreList)
                    updatedActivityScoreList.push(activityScoreList);
                else {
                    console.log("Activity not found");
                    return;
                }
            }
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
            newCol = col;
        }
        // if the cell is in a folded category, move to the next available cell
        if(direction === 'right')
        {
            while(foldedCategories[data[newRow][newCol].activityCategoryId] && newCol < data[newRow].length - 1)
            {
                newCol++;
            }
        }
        else if(direction === 'left')
        {
            while(foldedCategories[data[newRow][newCol].activityCategoryId] && newCol > 0)
            {
                newCol--;
            }
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
        <div ref={containerRef}>
            {isLoading && <EditableGridSkeleton></EditableGridSkeleton>}

            <div className=" border-collapse flex flex-col h-min transition-all duration-300">
                <div className=" flex flex-row pb-1">
                    <div className="shrink-0  w-40"></div>
                    <div className="flex flex-row ">
                        {categories.map((category, categoryIndex) => (
                            <div key={`category-${categoryIndex}`} className={`flex flex-col ${
                                foldedCategories[category.id]
                                    ? "order-last pop-in-out"
                                    : "order-none pop"
                            }`}>
                                <div
                                    className={`cursor-pointer order-none font-bold text-center bg-gradient-to-r from-blue-500 to-indigo-600 text-white py-1 px-2  mb-1`}
                                    onClick={() => toggleFoldCategory(category.id)}
                                >
                                    {category.name}
                                </div>
                                <div className="flex">
                                    {scoreData
                                        .filter((item) => item.activity.activityCategoryId === category.id)
                                        .map((item, index) => (
                                            <div
                                                key={item.activity.id}
                                                className={`w-24 border-collapse text-center whitespace-nowrap cursor-pointer transition-all duration-500 ease-in-out ${
                                                    foldedCategories[category.id]
                                                        ? "opacity-0 max-w-0 max-h-0 overflow-hidden"
                                                        : "opacity-100"
                                                } ${zoomedColumnIndex !== null && item.activity.id !== zoomedColumnIndex ? "opacity-50 overflow-hidden" : "opacity-100"} ${zoomedColumnIndex !== null && item.activity.id == zoomedColumnIndex ? "origin-top scale-105" : "origin-left scale-100"}`}
                                                onClick={() => {
                                                    // if the row is zoomed in, zoom out and zoom in the column
                                                    if (zoomedRowIndex !== null) {
                                                        setZoomedRowIndex(null);
                                                        setTimeout(() => {
                                                            setZoomedColumnIndex(zoomedColumnIndex === item.activity.id ? null : item.activity.id);
                                                        }, 200);
                                                    }
                                                    else {
                                                        setZoomedColumnIndex(zoomedColumnIndex === item.activity.id ? null : item.activity.id);
                                                    }
                                                }}
                                            >
                                                {item.activity.name}
                                                <p>0-{item.activity.maxScore}</p>
                                            </div>
                                        ))}
                                </div>
                            </div>
                        ))}
                    </div>

                </div>
                {data.map((row, rowIndex) => {
                    return (
                        <div key={`row-${rowIndex}`}
                             className={`border-collapse flex items-center cursor-pointer transition-all duration-500 ease-in-out ${zoomedRowIndex !== null && rowIndex !== zoomedRowIndex ? "opacity-50 max-h-20 overflow-hidden" : "opacity-100 max-h-20"} ${zoomedRowIndex !== null && rowIndex == zoomedRowIndex ? "origin-top scale-[1.03] z-10" : "origin-left scale-100 z-0"}`}>
                            <div
                                // names
                                className="shrink-0 text-center w-40"
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
                                     className={`shrink-0 w-24 border-collapse transition-all duration-500 ease-in-out ${zoomedColumnIndex !== null && cell.activityId !== zoomedColumnIndex ? "opacity-50 overflow-hidden" : "opacity-100"} ${zoomedColumnIndex !== null && cell.activityId == zoomedColumnIndex ? "origin-top scale-110 z-10" : "origin-bottom scale-100 z-0"} ${foldedCategories[cell.activityCategoryId]? "fade-out overflow-hidden" : "fade-in"}`}>
                                    <GridCell
                                        isZoomedIn={rowIndex === zoomedRowIndex || cell.activityId === zoomedColumnIndex}
                                        id={`cell-${rowIndex}-${colIndex}`}
                                        value={cell.score}
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
