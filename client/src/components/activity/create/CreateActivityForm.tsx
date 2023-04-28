import {useEffect, useState} from "react";
import * as actions from "./CreateActivityActions";
import {Duration} from "luxon";
import Cookies from "js-cookie";
import {buttonStyle, formStyle, inputStyle, labelStyle} from "../../../utils/formStyles";
import {useFormik} from "formik";
import * as Yup from "yup";
import * as formUtils from "../common/FormUtils";

const ActivityCreationForm: React.FC = () => {
  const [categories, setCategories] = useState([]);

  const styles = {
    inline: {
      display: "flex",
      marginLeft: "auto",
      marginRight: "auto",
      alignItems: "center",
    },
  };

  const authToken = Cookies.get("token");

  const validationSchema = Yup.object({
    name: Yup.string().required('Nazwa aktywności jest wymagana'),
    description: Yup.string().required('Opis aktywności jest wymagany'),
    maxScore: Yup.string().required('Maksymalna liczba punktów do zdobycia jest wymagana'),
    duration: Yup.string().required('Czas trwania aktywności jest wymagany'),
    activityTypeName: Yup.string().required('Typ krwinek do zdobycia jest wymagany'),
    activityCategoryName: Yup.string().required('Kategoria aktywności jest wymagana')
  });

  const formik = useFormik({
    initialValues: {
      name: "",
      description: "",
      duration:  Duration.fromObject({
        weeks: 0,
        days: 0,
        hours: 0,
        minutes: 0,
      }),
      maxScore: "",
      activityTypeName: "",
      activityCategoryName: "",
      courseGroupNames: [],
      courseGroupStartDates: [],
      groupName:"",
      groupStartDate: new Date(Date.now()),
    },
    onSubmit: values => {
      actions.createActivity(
          values.name,values.maxScore, values.description, formUtils.serializeDuration(formik),
          values.activityTypeName, values.activityCategoryName
          ,values.courseGroupNames,
          values.courseGroupStartDates, authToken);},
    validationSchema: validationSchema,
  });

  function resetForm(){
    setName("");
    setDescription("");
    setMaxScore(0);
    setDuration(Duration.fromObject({
      weeks: 0,
      days: 0,
      hours: 0,
      minutes: 0,
    }));
    setActivityTypeName("");
    setActivityCategoryName("");
    setGroupName("");
    setTerms([]);
    setCategories([]);

  }

  useEffect(() => {
    actions.getActivityCategories()
        .then((data) => setCategories(data));
  }, []);


  return (
      <div className="min-h-screen bg-gray-100 flex items-center justify-center">
        <form
            onSubmit={formik.handleSubmit}
            className={formStyle}
        >
          <fieldset>
            <h2 className="text-2xl font-bold mb-6">Dodaj aktywność</h2>
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
                      min="0"
                      style={styles.inline}
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
                  name="maxScore"
                  min="1"
                  value={formik.values.maxScore}
                  onChange={formik.handleChange}
                  className={inputStyle}
              />
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
                <option value="none">--Wybierz typ--</option>
                <option value="compulsory">obowiązkowe</option>
                <option value="optional">bonusowe</option>
                <option value="reparative">naprawcze</option>
              </select>
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
                {categories.map((category) => (
                    <option key={category.name} value={category.name}>
                      {category.name}
                    </option>
                ))}
              </select>
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
              <input
                  id="groupName"
                  name="groupName"
                  type="text"
                  value={formik.values.groupName}
                  onChange={formik.handleChange}
                  className={inputStyle}
              />
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
                  value={formik.values.groupStartDate}
                  onChange = {formUtils.handleGroupStartDateChange(formik)}
                  className={inputStyle}
              />
            </div>

            <a
                onClick={formUtils.handleAddTerm(formik)}
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
          </fieldset>
          <button
              type="submit"
              className={buttonStyle}
          >
            Utwórz aktywność
          </button>
        </form>
      </div>
  );
};
export default ActivityCreationForm;