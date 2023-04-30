import React, {useEffect, useState} from "react";
import * as actions from "./EditActivityActions";
import {DateTime, Duration} from "luxon";
import Cookies from "js-cookie";
import moment from "moment";
import {buttonStyle, errorStyle, formStyle, inputStyle, labelStyle} from "../../../utils/formStyles";
import {useFormik} from "formik";
import * as Yup from "yup";
import * as formUtils from "../common/FormUtils";
import * as termsFormikUtils from "../common/TermsFormik";

const ActivityEditForm: React.FC = (props) => {
  const [categories, setCategories] = useState([]);
  const [types, setTypes] = useState([]);
  const [activities,setActivites] = useState([]);
  const [groupNames, setGroupNames] = useState([]);

  const styles = {
    inline: {
      display: "flex",
      marginLeft: "auto",
      marginRight: "auto",
      alignItems: "center",
    },
  };

  const authToken = Cookies.get("token");
  let check = false;
  let tmpGroupNames;

  useEffect(() => {
    actions.getInstructorData().then((data)=>{
      setCategories(data.activityCategoryNames);
      setTypes(data.activityTypeNames);
      setActivites(data.activityNames);
      setGroupNames(data.groupNames);
    })
  }, []);

  const validationSchema = Yup.object({
    name: Yup.string()
        .required('Nazwa aktywności jest wymagana'),
    description: Yup.string().required('Opis aktywności jest wymagany'),
    maxScore: Yup.string().required('Maksymalna liczba punktów do zdobycia jest wymagana'),
    duration: Yup.object().test('at-least-one', 'Czas trwania jest wymagany', value => {
      const { weeks, days, hours, minutes } = value;
      return weeks !== 0 || days !== 0 || hours !== 0 || minutes !== 0;

    }).required('Czas trwania jest wymagany'),
    activityTypeName: Yup.string().required('Typ krwinek do zdobycia jest wymagany'),
    activityCategoryName: Yup.string().required('Kategoria aktywności jest wymagana'),
    courseGroupNames: Yup.array()
        .of(Yup.string())
        .test(
            'contains-all-group-names',
            'Wprowadzono niepoprawną liczbę grup',
            value => {
              if(!check){
                check = !check;
                tmpGroupNames = value;
              }
              return tmpGroupNames.length === groupNames.length && groupNames.every(groupName => tmpGroupNames.includes(groupName));
            }
        )
        .required('Terminy powinny być ustalone dla wszystkich grup'),
  });

  const formik = useFormik({
    initialValues: {
      convertedDuration: formUtils.convertDuration(props.editedActivity.duration),
      name: props.editedActivity.name,
      description: props.editedActivity.description,
      duration:  Duration.fromObject({
        weeks: formUtils.convertDuration(props.editedActivity.duration).weeks,
        days: formUtils.convertDuration(props.editedActivity.duration).days,
        hours: formUtils.convertDuration(props.editedActivity.duration).hours,
        minutes: formUtils.convertDuration(props.editedActivity.duration).minutes,
      }),
      maxScore: props.editedActivity.maxScore,
      activityTypeName: props.editedActivity.activityTypeName,
      activityCategoryName: props.editedActivity.activityCategoryName,
      courseGroupNames: props.editedActivity.courseGroupNames,
      courseGroupStartDates: props.editedActivity.courseGroupStartDates,
      groupName:"",
      groupStartDate: new Date(Date.now()),
    },
    onSubmit: values => {
      actions.editActivity(props.editedActivity.id,
          values.name,values.maxScore, values.description, formUtils.serializeDuration(formik),
          values.activityTypeName, values.activityCategoryName
          ,values.courseGroupNames,
          values.courseGroupStartDates, authToken);

      let updatedActivities = props.activities.filter(
          (activity: any) => activity.id !== props.editedActivity.id
      );

      let updatedActivity =  {
        id: props.editedActivity.id,
        name: values.name,
        maxScore: values.maxScore,
        description: values.description,
        duration: formUtils.serializeDuration(formik),
        activityTypeName: values.activityTypeName,
        activityCategoryName: values.activityCategoryName,
        courseGroupNames: values.courseGroupNames,
        courseGroupStartDates: values.courseGroupStartDates,
      };
      let result = [
        ...updatedActivities,
        updatedActivity
      ];
      props.setEditedActivity(updatedActivity);
      props.setActivities(result);
      props.closeEditActivityForm();
    },
    validationSchema: validationSchema,
  });

  const termsFormik = termsFormikUtils.termsFormik(formik);
  
  return (
      <div className="min-h-screen mt-8 bg-gray-100 flex items-center justify-center">
        <form
            onSubmit={formik.handleSubmit}
            className={formStyle}
        >
          {" "}
          <button
              style={{ float: "right" }}
              onClick={() => props.closeEditActivityForm()}
          >
            <i className="fas fa-times text-red-400"></i>
          </button>
          <fieldset>
            <h2 className="text-2xl font-bold mb-6">Edytuj aktywność</h2>
            <div className="mb-4">
              <label
                  htmlFor="name"
                  className={labelStyle}
              >
                Nazwa
              </label>

              <input
                  type="text"
                  id="name"
                  value={formik.values.name}
                  onChange={formik.handleChange}
                  className={inputStyle}
              />
              {formik.touched.name && formik.errors.name && (
                  <div className={errorStyle}>{formik.errors.name}</div>
              )}
            </div>

            <div className="mb-4">
              <label
                  htmlFor="duration"
                  className={labelStyle}
              >
                Czas trwania
              </label>
              <div id="duration">
                <div className="row" style={styles.inline}>
                  <label
                      htmlFor="weeks"
                      style={{ marginRight: "10px" }}
                      className={labelStyle}
                  >
                    Tygodnie:
                  </label>
                  <input
                      type="number"
                      id="weeks"
                      name="weeks"
                      min="0"
                      value={formik.values.duration.weeks}
                      className={inputStyle}
                      onChange={formUtils.handleWeeksChange(formik)}
                  />
                </div>

                <div className="row" style={styles.inline}>
                  <label
                      htmlFor="days"
                      style={{ marginRight: "10px" }}
                      className={labelStyle}
                  >
                    Dni:
                  </label>
                  <input
                      type="number"
                      id="days"
                      name="days"
                      style={styles.inline}
                      min="0"
                      value={formik.values.duration.days}
                      className={inputStyle}
                      onChange={formUtils.handleDaysChange(formik)}
                  />
                </div>

                <div className="row" style={styles.inline}>
                  <label
                      htmlFor="hours"
                      style={{ marginRight: "10px" }}
                      className={labelStyle}
                  >
                    Godziny:
                  </label>
                  <input
                      type="number"
                      id="hours"
                      name="hours"
                      style={styles.inline}
                      min="0"
                      max="23"
                      value={formik.values.duration.hours}
                      className={inputStyle}
                      onChange={formUtils.handleHoursChange(formik)}
                  />
                </div>
                <div className="row" style={styles.inline}>
                  <label
                      htmlFor="minutes"
                      style={{ marginRight: "10px" }}
                      className={labelStyle}
                  >
                    Minuty:
                  </label>
                  <input
                      type="number"
                      id="minutes"
                      name="minutes"
                      style={styles.inline}
                      min="0"
                      max="59"
                      value={formik.values.duration.minutes}
                      className={inputStyle}
                      onChange={formUtils.handleMinutesChange(formik)}
                  />
                </div>
                {formik.touched.duration && formik.errors.duration && (
                    <div className={errorStyle}>{formik.errors.duration}</div>
                )}
              </div>
            </div>

            <div className="mb-4">
              <label
                  htmlFor="maxScore"
                  className={labelStyle}
              >
                Maksymalna liczba punktów
              </label>
              <input
                  type="number"
                  id="maxScore"
                  min="1"
                  value={formik.values.maxScore}
                  onChange={formik.handleChange}
                  className={inputStyle}
              />
              {formik.touched.maxScore && formik.errors.maxScore && (
                  <div className={errorStyle}>{formik.errors.maxScore}</div>
              )}
            </div>

            <div className="mb-4">
              {" "}
              <label
                  htmlFor="description"
                  className={labelStyle}
              >
                Opis:
              </label>
              <textarea
                  id="description"
                  name="description"
                  value={formik.values.description}
                  onChange={formik.handleChange}
                  className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              ></textarea>
              {formik.touched.description && formik.errors.description && (
                  <div className={errorStyle}>{formik.errors.description}</div>
              )}
            </div>

            <div className="mb-4">
              {" "}
              <label
                  htmlFor="activityTypeName"
                  className={labelStyle}
              >
                Typ krwinek do zdobycia:
              </label>
              <select
                  id="activityTypeName"
                  name="activityTypeName"
                  value={formik.values.activityTypeName}
                  onChange={formik.handleChange}
                  className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              >
                <option value="">--Wybierz typ--</option>
                {types.map((typeName,index) => (
                    <option key={index} value={typeName}>
                      {typeName}
                    </option>
                ))}
              </select>
              {formik.touched.activityTypeName && formik.errors.activityTypeName && (
                  <div className={errorStyle}>{formik.errors.activityTypeName}</div>
              )}
            </div>

            <div className="mb-4">
              <label
                  htmlFor="activityCategoryName"
                  className={labelStyle}
              >
                Kategoria:
              </label>
              <select
                  id="activityCategoryName"
                  name="activityCategoryName"
                  value={formik.values.activityCategoryName}
                  onChange={formik.handleChange}
                  className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              >
                <option value="">--Wybierz kategorię aktywności--</option>
                {categories.map((categoryName,index) => (
                    <option key={index} value={categoryName}>
                      {categoryName}
                    </option>
                ))}
              </select>
              {formik.touched.activityCategoryName && formik.errors.activityCategoryName && (
                  <div className={errorStyle}>{formik.errors.activityCategoryName}</div>
              )}
            </div>
          </fieldset>
          <fieldset>
            <h2 className="text-xl font-bold mb-6 mt-6">
              Dodaj terminy rozpoczęcia aktywności dla poszczególnych grup
            </h2>

            <div className="mb-4">
              {" "}
              <label
                  htmlFor="groupName"
                  className={labelStyle}
              >
                Nazwa grupy:
              </label>
              <select
                  id="groupName"
                  name="groupName"
                  value={termsFormik.values.groupName}
                  onChange={termsFormik.handleChange}
                  className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
              >
                <option value="">--Wybierz grupę--</option>
                {groupNames.map((groupName,index) => (
                    <option key={index} value={groupName}>
                      {groupName}
                    </option>
                ))}
              </select>
              {termsFormik.touched.groupName && termsFormik.errors.groupName && (
                  <div className={errorStyle}>{termsFormik.errors.groupName}</div>
              )}
            </div>

            <div className="mb-4">
              {" "}
              <label
                  htmlFor="groupStartDate"
                  className={labelStyle}
              >
                Data rozpoczęcia:
              </label>
              <input
                  id="groupStartDate"
                  name="groupStartDate"
                  type="datetime-local"
                  value={termsFormik.values.groupStartDate}
                  onChange = {formUtils.handleGroupStartDateChange(termsFormik)}
                  className={inputStyle}
              />
              {termsFormik.touched.groupStartDate && termsFormik.errors.groupStartDate && (
                  <div className={errorStyle}>{termsFormik.errors.groupStartDate}</div>
              )}
            </div>

            <a
                onClick={termsFormik.handleSubmit}
                className="text-indigo-600 hover:text-indigo-800 font-medium text-center "
            >
              Dodaj termin
            </a>

            <table className="table-fixed w-full mb-6 mt-6">
              <thead>
              <tr>
                <th className="w-1/2 px-4 py-2">Grupa</th>
                <th className="w-1/2 px-4 py-2">Termin rozpoczęcia</th>
                <th className="w-1/2 px-4 py-2"></th>
              </tr>
              </thead>
              <tbody>
              {formik.values.courseGroupNames.map((groupName, index) => (
                  <tr key={index}>
                    <td className="block text-gray-700 font-thin text-center px-4 py-2">
                      {groupName}
                    </td>
                    <td className=" text-gray-700 font-thin text-center px-4 py-2">
                      {new Date(formik.values.courseGroupStartDates[index]).toLocaleString("pl-PL", {
                        year: "numeric",
                        month: "2-digit",
                        day: "2-digit",
                        hour: "2-digit",
                        minute: "2-digit",
                      })}
                    </td>
                    <td className=" px-4 py-2">
                      <button onClick={() => formUtils.handleDeleteTerm(index,formik)}>
                        <i className="fas fa-times text-indigo-600"></i>
                      </button>
                    </td>
                  </tr>
              ))}
              </tbody>
            </table>
            {formik.touched.courseGroupNames && formik.errors.courseGroupNames && (
                <div className={errorStyle}>{formik.errors.courseGroupNames}</div>
            )}
          </fieldset>
          <button
              type="submit"
              className={buttonStyle}
          >
            Edytuj aktywność
          </button>
        </form>
      </div>
  );
};
export default ActivityEditForm;