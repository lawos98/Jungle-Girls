import React, {useEffect, useState, useMemo} from "react";
import Cookies from "js-cookie";
import moment from "moment";
import ActivityEditForm from "../edit/EditActivityForm";
import Collapse from "@mui/material/Collapse";
import IconButton from "@mui/material/IconButton";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Typography from "@mui/material/Typography";
import Paper from "@mui/material/Paper";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import KeyboardArrowUpIcon from "@mui/icons-material/KeyboardArrowUp";
import * as actions from "./ActivityListActions";

function ActivityList() {
    const [activities, setActivities] = useState<[]>([]);
    const [editedActivity, setEditedActivity] = useState(false);
    const authToken = Cookies.get("token");

    useEffect(() => {
        actions.getActivities()
            .then((data) => {
                return setActivities(data);
            });
    }, []);


    function showEditActivityForm(activity: any): void {
        setEditedActivity(activity);
    }

    function closeEditActivityForm(): void {
        setEditedActivity(false);
    }

    function handleConvertDuration(duration) {
        const dur = moment.duration(duration);
        const weeks = dur.weeks();
        const days = dur.days() - weeks * 7;
        const hours = dur.hours();
        const minutes = dur.minutes();
        return [
            weeks && `${weeks} tyg.`,
            days && `${days} dni`,
            hours && `${hours} godz.`,
            minutes && `${minutes} min.`,
        ]
            .filter(Boolean)
            .join(" ");
    }

    function handleDeleteActivity(id: number): void {
        const payload = {
            id: id,
        };
        actions.deleteActivity(payload)
            .then(() => {
                setActivities(activities.filter((activity: any) => activity.id !== id));
            });
    }

    function getTranslatedActivityType(type) {
        const translations = {
            compulsory: "obowiązkowe",
            optional: "bonusowe",
            reparative: "naprawcze",
        };
        return translations[type] || type;
    }

    function Row(props: { row }) {
        const {row} = props;
        const [open, setOpen] = React.useState(false);

        return (
            <>
                <TableRow sx={{"& > *": {borderBottom: "unset"}}}>
                    <TableCell>
                        <IconButton
                            aria-label="expand row"
                            size="small"
                            onClick={() => setOpen(!open)}
                        >
                            {open ? <KeyboardArrowUpIcon/> : <KeyboardArrowDownIcon/>}
                        </IconButton>
                    </TableCell>
                    <TableCell component="th" scope="row">
                        {row.name}
                    </TableCell>
                    <TableCell>{row.description}</TableCell>
                    <TableCell>
                        <button onClick={() => handleDeleteActivity(row.id)}>
                            <i className="fas fa-times text-indigo-400"></i>
                        </button>
                    </TableCell>
                    <TableCell>
                        <button onClick={() => showEditActivityForm(row)}>
                            <i className="fas fa-pencil-alt text-indigo-400"></i>
                        </button>
                    </TableCell>
                </TableRow>
                <TableRow>
                    <TableCell style={{paddingBottom: 0, paddingTop: 0}} colSpan={6}>
                        <Collapse in={open} timeout="auto" unmountOnExit>

                            <Typography variant="h6" gutterBottom component="div">
                                Szczegóły aktywności:
                            </Typography>
                            <Table size="small" aria-label="purchases">
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Maksymalna ilość punktów</TableCell>
                                        <TableCell>Czas trwania aktywności</TableCell>
                                        <TableCell>Typ krwinek do zdobycia</TableCell>
                                        <TableCell>Kategoria aktywności</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    <TableRow>
                                        <TableCell component="th" scope="row">
                                            {row.maxScore}
                                        </TableCell>
                                        <TableCell>{handleConvertDuration(row.duration)}</TableCell>
                                        <TableCell>{getTranslatedActivityType(row.activityTypeName)}</TableCell>
                                        <TableCell>
                                            {row.activityCategoryName}
                                        </TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>

                            <Table size="small" aria-label="purchases" className="mt-10">
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Nazwa grupy</TableCell>
                                        <TableCell>Termin rozpoczęcia aktywności</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {row.courseGroupNames.map((e, i) => (
                                        <TableRow key={i}>
                                            <TableCell component="th" scope="row">
                                                {e}
                                            </TableCell>
                                            <TableCell>{new Date(row.courseGroupStartDates[i]).toLocaleString("pl-PL", {
                                                year: "numeric",
                                                month: "2-digit",
                                                day: "2-digit",
                                                hour: "2-digit",
                                                minute: "2-digit",
                                            })}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>

                        </Collapse>
                    </TableCell>
                </TableRow>
            </>
        );
    }

    return (
        <div className="min-h-screen bg-gray-100 flex  flex-col items-center justify-center">
            <h1 className="text-3xl font-bold mb-6 mt-6">Aktywności</h1>
            <TableContainer component={Paper} style={{width: 800}}>
                <Table aria-label="collapsible table">
                    <TableHead>
                        <TableRow>
                            <TableCell/>
                            <TableCell>Nazwa</TableCell>
                            <TableCell>Opis</TableCell>
                            <TableCell></TableCell>
                            <TableCell></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {activities.map((activity) => (
                            <Row key={activity.id} row={activity}/>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <div>
                {editedActivity && (
                    <ActivityEditForm
                        editedActivity={editedActivity}
                        setEditedActivity={setEditedActivity}
                        setActivities={setActivities}
                        activities={activities}
                        closeEditActivityForm={closeEditActivityForm}
                    ></ActivityEditForm>
                )}
            </div>
        </div>
    );
}

export default ActivityList;
