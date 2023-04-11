package pl.edu.agh.ii.io.jungleGirls.dto

data class CreateActivityCategoryDto(
    val name: String,
    val description: String
)
data class DeleteActivityCategoryDto(
    val name: String
)
