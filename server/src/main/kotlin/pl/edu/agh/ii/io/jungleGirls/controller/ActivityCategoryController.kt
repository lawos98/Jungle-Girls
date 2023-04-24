package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.CreateActivityCategoryRequest
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityCategoryResponse
import pl.edu.agh.ii.io.jungleGirls.dto.DeleteActivityCategoryRequest
import pl.edu.agh.ii.io.jungleGirls.model.ActivityCategory
import pl.edu.agh.ii.io.jungleGirls.service.ActivityCategoryService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService

@RestController
@RequestMapping("api/activity-category")
class ActivityCategoryController(
    private val activityCategoryService: ActivityCategoryService,
    private val tokenService: TokenService
) {

    @GetMapping()
    fun getAllActivityCategories(@RequestHeader("Authorization") token: String):List<ActivityCategory>{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        return activityCategoryService.getAllActivityCategoriesByInstructorId(user.id!!)
    }


    @PostMapping("/create")
    fun createActivityCategory(@RequestBody payload: CreateActivityCategoryRequest, @RequestHeader("Authorization") token: String): String {
        val user = tokenService.parseToken(token.substring("Bearer".length))

        val activityCategory = ActivityCategory(
            name = payload.name,
            description = payload.description,
            instructorId = user.id!!
        )

        when(val result = activityCategoryService.createCategory(activityCategory)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> return "Activity category created successfully"
        }
    }
//    @PostMapping("/delete")
//    fun deleteActivityCategory(@RequestBody payload: DeleteActivityCategoryRequest, @RequestHeader("Authorization") token: String): String {
//        val user = tokenService.parseToken(token.substring("Bearer".length))
//        val categoryToDelete = activityCategoryService.findByInstructorIdAndName(user.id!!,payload.name)
//            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist")
//
//        return when(val result = activityCategoryService.deleteCategory(categoryToDelete)){
//            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
//            is Either.Right -> "Activity category deleted successfully"
//        }
//    }

        @GetMapping("/create","/edit/{*}")
    fun getAllActivityCategoryNames(@RequestHeader("Authorization") token: String): ActivityCategoryResponse{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        return ActivityCategoryResponse(activityCategoryService.getAllNames(user.id!!))
    }


    @PutMapping("/delete/{id}")
    fun deleteActivityCategory(@RequestHeader("Authorization") token: String, @PathVariable id: Long): String {
        val user = tokenService.parseToken(token.substring("Bearer".length))
        val categoryToDelete = activityCategoryService.findByIdAndInstructorId(id,user.id!!)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist")

        return when(val result = activityCategoryService.deleteCategory(categoryToDelete)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> "Activity category deleted successfully"
        }
    }

    @PutMapping("/edit/{id}")
    fun editActivityCategory(@RequestBody payload: CreateActivityCategoryRequest, @RequestHeader("Authorization") token: String, @PathVariable id: Long):String{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        val categoryToEdit = activityCategoryService.findByIdAndInstructorId(id,user.id!!)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist")

        val editedCategory = ActivityCategory(
            name = payload.name,
            description = payload.description,
            instructorId = user.id
        )

        return when(val result = activityCategoryService.editCategory(categoryToEdit,editedCategory)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> "Activity category edited successfully"
        }
    }
}
