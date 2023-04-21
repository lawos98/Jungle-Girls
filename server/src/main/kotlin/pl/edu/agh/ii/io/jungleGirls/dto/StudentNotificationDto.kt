package pl.edu.agh.ii.io.jungleGirls.dto



data class StudentNotificationResponse(
        val id : Long,
        val date : String,
        val subject : String,
        val content : String,
        val author : String,
        val wasRead : Boolean,
)