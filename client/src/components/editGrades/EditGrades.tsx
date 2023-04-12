import React from 'react';
import EditableGrid from "../editableGrid/EditableGrid";
import "./EditGrades.css";


function EditGrades() {
    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-2xl font-bold mb-4">Tu będzie się wpisywać ocenki</h1>
            <EditableGrid />
        </div>
    );
}

export default EditGrades;