// src/components/EditableGrid.tsx

import React, { useState } from 'react';
import {useTable, Column, CellProps} from 'react-table';
import { Dialog, Transition } from '@headlessui/react';

interface Grade {
    activity: string;
    grade: number;
}

interface Student {
    name: string;
    grades: Grade[];
}

interface EditableGridProps {
    students: Student[];
    activities: string[];
}

export const EditableGrid: React.FC<EditableGridProps> = ({ students, activities }) => {
    const [open, setOpen] = useState(false);
    const [selectedStudent, setSelectedStudent] = useState<Student | null>(null);

    const data = students.map((student) => ({
        ...student,
        ...Object.fromEntries(student.grades.map((grade) => [grade.activity, grade.grade])),
    }));

    const columns: Column<any>[] = React.useMemo(
        () => [
            {
                Header: 'Student',
                accessor: 'name',
            },
            ...activities.map((activity) => ({
                Header: activity,
                accessor: activity,
                Cell: ({ value, row, column }: CellProps<any, number>) => (
                    <input
                        type="number"
                        className="w-full border border-gray-300 px-2 py-1"
                        value={value}
                        onChange={(e) => {
                            const newGrade = parseFloat(e.target.value);
                            row.values[column.id] = isNaN(newGrade) ? 0 : newGrade;
                        }}
                    />
                ),
            })),
        ],
        [activities]
    );
    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow,
    } = useTable({ columns, data });

    return (
        <div>
            <div> hi :)</div>
            <table {...getTableProps()} className="w-full">
                <thead>
                {headerGroups.map((headerGroup) => (
                    <tr {...headerGroup.getHeaderGroupProps()}>
                        {headerGroup.headers.map((column) => (
                            <th
                                {...column.getHeaderProps()}
                                className="border px-2 py-1 text-left"
                            >
                                {column.render('Header')}
                            </th>
                        ))}
                    </tr>
                ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                {rows.map((row) => {
                    prepareRow(row);
                    return (
                        <tr
                            {...row.getRowProps()}
                            onClick={() => {
                                // setSelectedRow(row.original as Grade);
                                setSelectedStudent(row.original as Student);
                                setOpen(true);
                            }}
                            className="cursor-pointer"
                        >
                            {row.cells.map((cell) => (
                                <td
                                    {...cell.getCellProps()}
                                    className="border px-2 py-1 text-left"
                                >
                                    {cell.render('Cell')}
                                </td>
                            ))}
                        </tr>
                    );
                })}
                </tbody>
            </table>
            {selectedStudent && (
                <Transition show={open} as={React.Fragment}>
                    <Dialog
                        as="div"
                        className="fixed inset-0 z-10 overflow-y-auto"
                        onClose={() => setOpen(false)}
                    >
                        <Dialog.Overlay className="fixed inset-0 bg-black opacity-30" />
                        <div className="min-h-screen px-4 text-center">
                            <Dialog.Title className="font-bold text-2xl mb-4">
                                {selectedStudent.name}
                            </Dialog.Title>
                            <table className="w-full">
                                {/* ... */}
                            </table>
                            <div className="mt-4">
                                <button
                                    type="button"
                                    className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded mr-2"
                                    onClick={() => setOpen(false)}
                                >
                                    Save
                                </button>
                                <button
                                    type="button"
                                    className="bg-gray-300 hover:bg-gray-400 text-black px-4 py-2 rounded"
                                    onClick={() => setOpen(false)}
                                >
                                    Cancel
                                </button>
                            </div>
                        </div>
                    </Dialog>
                </Transition>
            )}
        </div>
    );
};

export default EditableGrid;