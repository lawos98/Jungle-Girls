package pl.edu.agh.ii.io.jungleGirls.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class CreateActivityDto (
    val name: String,
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    val startDate: LocalDateTime,
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    val endDate: LocalDateTime,
    val maxScore: Double,
    val description: String,
    val courseGroupNames: ArrayList<String>,
    val activityTypeName: String,
    val activityCategoryName: String,
)


