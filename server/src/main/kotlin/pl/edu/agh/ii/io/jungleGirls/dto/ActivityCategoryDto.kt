package pl.edu.agh.ii.io.jungleGirls.dto

data class CreateActivityCategoryRequest (
    val name: String,
    val description: String,
)
data class DeleteActivityCategoryRequest (
    val name: String,
)

data class ActivityCategoryResponse (
    val name: ArrayList<String>,
)