import React from 'react';
// TODO check how to import correctly
import EditableGrid from "../editableGrid/EditableGrid";

const students = [
    {
        name: 'John Doe',
        grades: [
            { activity: 'Homework', grade: 10 },
            { activity: 'Quiz', grade: 8 },
            { activity: 'Test', grade: 9 },
        ],
    },
    {
        name: 'Jane Doe',
        grades: [
            { activity: 'Homework', grade: 9 },
            { activity: 'Quiz', grade: 10 },
            { activity: 'Test', grade: 10 },
        ],
    },
];

const activities = ['Homework', 'Quiz', 'Test'];

function EditGrades() {
    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-2xl font-bold mb-4">Grade Management System</h1>
            <EditableGrid students={students} activities={activities} />
        </div>
    );
}

export default EditGrades;