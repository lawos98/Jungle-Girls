import React, {useEffect, useState} from "react";
import EditableGrid from "../editableGrid/EditableGrid";
import "./EditGrades.css";
import { Tab } from "@headlessui/react";
import * as actions from "./EditGradesActions";
function classNames(...classes: any[]) {
    return classes.filter(Boolean).join(" ");
}

function EditGrades() {
    const [groupIds, setGroupIds] = useState<number[]>([]);
    useEffect(() => {
        actions.getGroups((groups:any)=> setGroupIds(groups.groupIds) )
    }, []);
    return (
        <div className="container mx-auto px-4 py-8 w-8/12">
            <div className=" w-full px-2 sm:px-0">
                <Tab.Group>
                    <Tab.List className="w-1/3 h-1/3 border border-orange-900 flex space-x-1 rounded-xl bg-blue-900/20 p-1">
                        {Array.isArray(groupIds) && groupIds.map((id) => (
                            <Tab
                                key={id}
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
                                {id}
                            </Tab>
                        ))}
                    </Tab.List>
                    <Tab.Panels className="my-8">
                        {groupIds.map((id) => (
                            <Tab.Panel key={id}>
                                <EditableGrid groupId={id} />
                            </Tab.Panel>
                        ))}
                    </Tab.Panels>
                </Tab.Group>
            </div>
        </div>
    );
}

export default EditGrades;