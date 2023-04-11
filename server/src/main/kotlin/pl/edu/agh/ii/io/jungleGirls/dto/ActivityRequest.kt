package pl.edu.agh.ii.io.jungleGirls.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.Duration
import java.time.LocalDateTime

data class CreateActivityDto(
    val name: String,
    val description: String,
    val duration: Duration,
    val maxScore: Double,
    val courseGroupNames: ArrayList<String>,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val courseGroupStartDates: ArrayList<LocalDateTime>,
    val activityTypeName: String,
    val activityCategoryName: String
)

data class DeleteActivityDto(
    val name: String
)
