package pl.edu.agh.ii.io.jungleGirls.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.Duration
import java.time.LocalDateTime

data class CreateActivityRequest (
    val name: String,
    val description: String,
    val duration: Duration,
    val maxScore: Double,
    val courseGroupNames: ArrayList<String>,
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    val courseGroupStartDates: ArrayList<LocalDateTime>,
    val activityTypeName: String,
    val activityCategoryName: String,
)

data class ActivityRequest(
    val id: Long,
    val name: String,
    val description: String,
    val duration: Duration,
    val maxScore: Double,
    val courseGroupNames: ArrayList<String>,
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    val courseGroupStartDates: ArrayList<LocalDateTime>,
    val activityTypeName: String,
    val activityCategoryName: String,
)

data class DeleteActivityRequest (
    val name: String,
)

data class ActivityCreationResponse(
    val groupNames: ArrayList<String>,
    val activityTypeNames: ArrayList<String>,
    val activityCategoryNames: ArrayList<String>,
    val activityNames: ArrayList<String>
)

data class ActivityDeletionResponse(
    val activityNames: ArrayList<String>
)

data class EditActivityRequest(
    val name: String,
    val description: String,
    val duration: Duration,
    val maxScore: Double,
)
