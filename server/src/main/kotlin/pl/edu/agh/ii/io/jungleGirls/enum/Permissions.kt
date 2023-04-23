package pl.edu.agh.ii.io.jungleGirls.enum

enum class Permissions(val permissionName:String) {
    USERS_MANAGEMENT("Users manager"), // User management: giving roles,changing permissions, etc.
    GRADE_VIEW("Grade view"), // To see grades for all groups
    GRADE_EDIT("Grade edit"), // To edit grades for all groups
    ACTIVITY_MANAGEMENT("Activity manager"), // To add/edit/delete activities
    ACTIVITY_CATEGORY_MANAGEMENT("Activity category manager"), // To add/edit/delete activity categories
    NOTIFICATION_VIEW("Notification view") // To see notifications and edit wasRead field

}
