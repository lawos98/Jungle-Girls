package pl.edu.agh.ii.io.jungleGirls

import pl.edu.agh.ii.io.jungleGirls.Config.activityCategoryFile
import pl.edu.agh.ii.io.jungleGirls.Config.activityFile
import pl.edu.agh.ii.io.jungleGirls.Config.courseGroupFile
import pl.edu.agh.ii.io.jungleGirls.Config.instructorFile
import pl.edu.agh.ii.io.jungleGirls.Config.numberOfActivitiesPerActivityCategory
import pl.edu.agh.ii.io.jungleGirls.Config.rolePermissionFile
import pl.edu.agh.ii.io.jungleGirls.Config.studentFile
import pl.edu.agh.ii.io.jungleGirls.Config.numberOfGroupsPerInstructor
import pl.edu.agh.ii.io.jungleGirls.Config.numberOfInstructors
import pl.edu.agh.ii.io.jungleGirls.Config.numberOfStudentsPerGroup
import pl.edu.agh.ii.io.jungleGirls.enum.Permissions

fun main(args: Array<String>) {
    println(args)

    studentFile.writeText("")
    activityCategoryFile.writeText("")
    rolePermissionFile.writeText("")
    courseGroupFile.writeText("")
    activityCategoryFile.writeText("")
    activityFile.writeText("")
    instructorFile.writeText("")

    for(roleName in Permissions.values().map { it.getName() }){
        rolePermissionFile.appendText("insert into permission(name) values ('$roleName');\n")
    }
    rolePermissionFile.appendText("\n")
    for(roleId in 1..4) {
        val permissionNumber = Permissions.values().size
        for(permissionId in 1..permissionNumber){
            rolePermissionFile.appendText("insert into role_permission(role_id, permission_id,should_be_displayed) values ($roleId,$permissionId,${roleId==4});\n")
        }
    }

    studentFile.appendText("\n")
    for(instructorNumber in 1..numberOfInstructors){
        for(groupNumber in 1..numberOfGroupsPerInstructor){
            val groupId = (instructorNumber-1)* numberOfGroupsPerInstructor+groupNumber
            courseGroupFile.appendText("insert into course_group(name,instructor_id,secret_code) values ('group$groupId',$instructorNumber,null);\n")
            for(studentsNumber in 1..numberOfStudentsPerGroup){
                val studentId=studentsNumber+(groupNumber-1)*numberOfStudentsPerGroup+(instructorNumber-1)* numberOfGroupsPerInstructor* numberOfStudentsPerGroup
                studentFile.appendText("insert into login_user(username,password,firstname,lastname,role_id) values ('student$studentId','\$2a\$10\$bzGDiQIL/YVOFAGXrAIXk.7hf4JioELIsmy6kwT39Gn4i.Q.CHpTy','student$studentId','student$studentId',2);\n")
                studentFile.appendText("insert into student_description(id,index,github_link,course_group_id) values (${studentId+ numberOfInstructors},null,null,$groupId);\n")
            }
        }
    }

    for(instructorNumber in 1..numberOfInstructors){
        instructorFile.appendText("insert into login_user(username,password,firstname,lastname,role_id) values ('mod$instructorNumber','\$2a\$10\$XwMIl8yC/TxaD4Xzpmla.OntZ3cnYIeW7OGSxFjCI3NVS3BY7KsWO','mod$instructorNumber','mod$instructorNumber',3);\n")
        activityCategoryFile.appendText("insert into activity_category(name, description, instructor_id)\n" +
                "values\n" +
                "    ('praca na zajeciach','wykonanie wszystkich cwiczen, aktywne uczestnictwo w zajeciach',$instructorNumber),\n" +
                "    ('wejsciowki','sprawdzenie czy student przyswoil sobie material z poprzednich labow',$instructorNumber),\n" +
                "    ('zadanie domowe','samodzielna praca domowa',$instructorNumber),\n" +
                "    ('inne','inne',$instructorNumber);")
        for(activityNumber in 1..numberOfActivitiesPerActivityCategory){
            val activityCategoryBaseId = (instructorNumber-1)* 4
            activityFile.appendText("insert into activity(name, duration, max_score, description, activity_type_id, activity_category_id) values ('kartkowka$activityNumber','PT12H',10,'kartkowka$activityNumber',1,${activityCategoryBaseId+1});\n")
            activityFile.appendText("insert into activity(name, duration, max_score, description, activity_type_id, activity_category_id) values ('zadanie$activityNumber','PT1H',2.5,'zadanie$activityNumber',1,${activityCategoryBaseId+2});\n")
            activityFile.appendText("insert into activity(name, duration, max_score, description, activity_type_id, activity_category_id) values ('domowe$activityNumber','PT24H',12.5,'domowe$activityNumber',1,${activityCategoryBaseId+3});\n")
            activityFile.appendText("insert into activity(name, duration, max_score, description, activity_type_id, activity_category_id) values ('plusy$activityNumber','PT24H',0.5,'plusy$activityNumber',1,${activityCategoryBaseId+4});\n")
            activityFile.appendText("\n")
            for(groupNumber in 1..numberOfGroupsPerInstructor){
                val groupId = (instructorNumber-1)* numberOfGroupsPerInstructor+groupNumber
                val activityId = (instructorNumber-1)* 4* numberOfActivitiesPerActivityCategory+4*(activityNumber-1)
                activityFile.appendText("insert into course_group_activity(activity_id, course_group_id, start_date) values (${activityId+1},$groupId,'2024-05-05T12:00:00');\n")
                activityFile.appendText("insert into course_group_activity(activity_id, course_group_id, start_date) values (${activityId+2},$groupId,'2024-05-12T15:00:00');\n")
                activityFile.appendText("insert into course_group_activity(activity_id, course_group_id, start_date) values (${activityId+3},$groupId,'2024-05-16T20:00:00');\n")
                activityFile.appendText("insert into course_group_activity(activity_id, course_group_id, start_date) values (${activityId+4},$groupId,'2024-05-20T20:00:00');\n")
                activityFile.appendText("\n")
            }
        }
    }



    println("\u001B[38;5;206m===============DONE===============\u001B[0m")
}