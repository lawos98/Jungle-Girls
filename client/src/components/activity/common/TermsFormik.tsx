import * as Yup from "yup";
import moment from "moment";
import {FormikProps, useFormik} from "formik";
import {DateTime, Duration} from "luxon";


const termsValidationSchema = Yup.object({
    groupName: Yup.string().required('Nazwa grupy jest wymagana'),
    groupStartDate: Yup.string().test('isValid', 'Data rozpoczęcia aktywności jest wymagana', function (value) {
        return moment(value, 'yyyy-MM-dd HH:mm:ss', false).isValid();
    })
        .required('Data rozpoczęcia aktywności jest wymagana').test('isAfter', 'Data rozpoczęcia aktywności musi być późniejsza niż aktualna data', function (value) {
            const currentDate = moment();
            return moment(value, 'yyyy-MM-dd HH:mm:ss', false).isAfter(currentDate);
        }),
});

export const termsFormik = (formik: FormikProps<FormValues>) => useFormik({
    initialValues: {
        groupName: "",
        groupStartDate: new DateTime(Date.now()),
    },
    onSubmit: values => {
        const newGroupNames = [...formik.values.courseGroupNames, groupName.value];
        const newGroupStartDates = [...formik.values.courseGroupStartDates, DateTime.fromISO(groupStartDate.value).toFormat('yyyy-MM-dd HH:mm:ss')];
        formik.setFieldValue('courseGroupNames',newGroupNames);
        formik.setFieldValue('courseGroupStartDates',newGroupStartDates);
        formik.setFieldValue('groupStartDate',new Date(Date.now()));
        formik.setFieldValue('groupName',"");
    },
    validationSchema: termsValidationSchema
});