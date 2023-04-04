package pl.edu.agh.ii.io.jungleGirls.dto

data class ActivityCreationResponseDto(
    val groupNames: ArrayList<String>,
    val activityTypeNames: ArrayList<String>,
    val activityCategoryNames: ArrayList<String>
)