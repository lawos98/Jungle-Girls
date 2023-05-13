import {DateTime, Duration} from "luxon";
import {FormikProps} from "formik";
import moment from "moment";

export const handleWeeksChange = (formik: FormikProps<FormValues>) => (event: React.ChangeEvent<HTMLInputElement>) =>  {
    const updatedDuration = formik.values.duration.set({ weeks: event.target.valueAsNumber });
    formik.setFieldValue("duration", updatedDuration);
};

export const handleDaysChange = (formik: FormikProps<FormValues>) => (event: React.ChangeEvent<HTMLInputElement>) => {
    const updatedDuration = formik.values.duration.
        set({ days: event.target.valueAsNumber });
    formik.setFieldValue("duration", updatedDuration);
};

export const handleHoursChange = (formik: FormikProps<FormValues>) => (event: React.ChangeEvent<HTMLInputElement>) => {
    const updatedDuration =  formik.values.duration.set({ hours: event.target.valueAsNumber });
    formik.setFieldValue("duration", updatedDuration);
};

export const handleMinutesChange = (formik: FormikProps<FormValues>) => (event: React.ChangeEvent<HTMLInputElement>) => {
    const updatedDuration =  formik.values.duration.set({ minutes: event.target.valueAsNumber });
    formik.setFieldValue("duration", updatedDuration);
};

export const handleGroupStartDateChange = (formik: FormikProps<FormValues>) => (event: React.ChangeEvent<HTMLInputElement>) => {
    formik.setFieldValue("groupStartDate", DateTime.fromISO(event.target.value).toFormat("yyyy-MM-dd HH:mm:ss"));
};

export const handleCourseGroupChange = (formik: FormikProps<FormValues>,termsFormik: FormikProps<FormValues>) => (event: React.ChangeEvent<HTMLInputElement>) => {
    const newGroupNames = [...formik.values.courseGroupNames, termsFormik.values.groupName];
    const newGroupStartDates = [...formik.values.courseGroupStartDates,termsFormik.values.groupStartDate];
    formik.setFieldValue("courseGroupNames",newGroupNames);
    formik.setFieldValue("courseGroupStartDates",newGroupStartDates);
};

export function serializeDuration(formik: FormikProps<FormValues>){
    return Duration.fromObject({
        days: formik.values.duration.days + formik.values.duration.weeks * 7,
        hours: formik.values.duration.hours,
        minutes: formik.values.duration.minutes,
    });
}

export function convertDuration(duration) {
    const dur = moment.duration(duration);
    const weeks = dur.weeks();
    const days = dur.days() - weeks * 7;
    const hours = dur.hours();
    const minutes = dur.minutes();
    return {weeks: weeks, days: days, hours: hours, minutes: minutes};
}


export function handleDeleteTerm(index: number,formik: FormikProps<FormValues>) {
    const newGroupNames = [...formik.values.courseGroupNames];
    const newGroupStartDates = [...formik.values.courseGroupStartDates];

    newGroupNames.splice(index, 1);
    newGroupStartDates.splice(index, 1);
    formik.setFieldValue("courseGroupNames",newGroupNames);
    formik.setFieldValue("courseGroupStartDates",newGroupStartDates);
}