package pl.edu.agh.ii.io.jungleGirls

import java.io.File

object Config {
    val rolePermissionFile = File("../migrations/v04_add_role_permission.sql")
    val instructorFile = File("../migrations/v05_add_instructor.sql")
    val courseGroupFile = File("../migrations/v06_add_course_group.sql")
    val studentFile = File("../migrations/v07_add_students.sql")
    val activityCategoryFile = File("../migrations/v08_add_activity_category.sql")
    val activityFile = File("../migrations/v09_add_activity.sql")

    val firstNames = listOf(
        "Jan", "Katarzyna", "Anna", "Maria", "Michał",
        "Piotr", "Krystyna", "Barbara", "Tomasz", "Adam",
        "Małgorzata", "Joanna", "Marek", "Andrzej", "Aleksandra"
    )

    val lastNames = listOf(
        "Kowalski", "Nowak", "Wiśniewski", "Wójcik", "Kowalczyk",
        "Kaczmarek", "Zieliński", "Szymański", "Wojciechowski", "Adamczyk",
        "Dąbrowski", "Pawlak", "Górski", "Jabłoński", "Kwiatkowski"
    )

    const val numberOfGroupsPerInstructor = 3
    const val numberOfInstructors = 4
    const val numberOfStudentsPerGroup = 10
    const val numberOfActivitiesPerActivityCategory = 3
}