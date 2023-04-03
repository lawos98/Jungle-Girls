package pl.edu.agh.ii.io.jungleGirls.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class CreateActivityCategoryDto (
    val name: String,
    val description: String,
)