import React from "react";
import EditableGrid from "../editableGrid/EditableGrid";
import "./EditGrades.css";
import { Tab } from "@headlessui/react";
function classNames(...classes: any[]) {
    return classes.filter(Boolean).join(" ");
}

function EditGrades() {
    return (
        <div className="container mx-auto px-4 py-8 w-8/12">
            <div className=" w-full px-2 sm:px-0">
                <Tab.Group>
                    <Tab.List className="w-1/3 h-1/3 border border-orange-900 flex space-x-1 rounded-xl bg-blue-900/20 p-1">
                        <Tab
                            className={({ selected }) =>
                                classNames(
                                    "w-full rounded-lg py-2.5 text-sm font-medium leading-5 text-blue-700",
                                    "ring-white ring-opacity-60 ring-offset-2 ring-offset-blue-400 focus:outline-none focus:ring-2",
                                    selected
                                        ? "bg-white shadow"
                                        : "text-blue-100 hover:bg-white/[0.12] hover:text-white"
                                )
                            }
                        >
                            1
                        </Tab><Tab
                            className={({ selected }) =>
                                classNames(
                                    "w-full rounded-lg py-2.5 text-sm font-medium leading-5 text-blue-700",
                                    "ring-white ring-opacity-60 ring-offset-2 ring-offset-blue-400 focus:outline-none focus:ring-2",
                                    selected
                                        ? "bg-white shadow"
                                        : "text-blue-100 hover:bg-white/[0.12] hover:text-white"
                                )
                            }
                        >
                        2
                        </Tab>
                    </Tab.List>
                    <Tab.Panels className="my-8">
                        <Tab.Panel><EditableGrid groupId={1} /></Tab.Panel>
                        <Tab.Panel><EditableGrid groupId={2} /></Tab.Panel>
                    </Tab.Panels>
                </Tab.Group>
            </div>
        </div>
    );
}

export default EditGrades;