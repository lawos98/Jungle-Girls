package pl.edu.agh.ii.io.jungleGirls.dto



data class StudentNotificationResponse(
        val id : Long,
        val date : String,
        val subject : String,
        val content : String,
        val author : String,
        val wasRead : Boolean,
)
data class StudentNotificationRequest(
    val subject: String,
    val content: String,
    val studentIds: List<Long>,
)

data class CourseGroupNotificationRequest(
    val subject: String,
    val content: String,
    val groupIds: List<Long>,
)

data class StudentNotificationStudentRequest(
    val id : Long,
    val firstname : String,
    val lastname : String,
    val index : Long?,
)
