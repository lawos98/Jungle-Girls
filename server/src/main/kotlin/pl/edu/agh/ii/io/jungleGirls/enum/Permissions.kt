package pl.edu.agh.ii.io.jungleGirls.enum

enum class Permissions {
    USERS_MANAGEMENT, // User management: giving roles,changing permissions, etc.
    GRADE_VIEW, // To see grades for all groups
    GRADE_EDIT, // To edit grades for all groups
    ACTIVITY_MANAGEMENT, // To add/edit/delete activities
    ACTIVITY_CATEGORY_MANAGEMENT, // To add/edit/delete activity categories
    NOTIFICATION_VIEW, // To see notifications and edit wasRead field
    CSV_GENERATION // To generate csv for all groups
    ;

    fun getName():String{
        return when(this){
            USERS_MANAGEMENT -> "Users manager"
            GRADE_VIEW -> "Grade view"
            GRADE_EDIT -> "Grade edit"
            ACTIVITY_MANAGEMENT -> "Activity manager"
            ACTIVITY_CATEGORY_MANAGEMENT -> "Activity category manager"
            NOTIFICATION_VIEW -> "Notification view"
            CSV_GENERATION -> "CSV generation"

        }
    }
}
