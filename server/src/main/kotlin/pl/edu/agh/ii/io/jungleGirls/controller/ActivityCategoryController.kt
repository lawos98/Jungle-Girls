package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.CreateActivityCategoryDto
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityCategoryResponseDto
import pl.edu.agh.ii.io.jungleGirls.dto.DeleteActivityCategoryDto
import pl.edu.agh.ii.io.jungleGirls.model.ActivityCategory
import pl.edu.agh.ii.io.jungleGirls.service.ActivityCategoryService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService

@RestController
@RequestMapping("api/activity_category")
class ActivityCategoryController(
    private val activityCategoryService: ActivityCategoryService,
    private val tokenService: TokenService
) {

    @PostMapping("/create")
    fun createActivityCategory(@RequestBody payload: CreateActivityCategoryDto, @RequestHeader("Authorization") token: String): String {
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
    @PostMapping("/delete")
    fun deleteActivityCategory(@RequestBody payload: DeleteActivityCategoryDto, @RequestHeader("Authorization") token: String): String {
        val user = tokenService.parseToken(token.substring("Bearer".length))
        val categoryToDelete = activityCategoryService.findByInstructorIdAndName(user.id!!,payload.name)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist")

        return when(val result = activityCategoryService.deleteCategory(categoryToDelete)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> "Activity category deleted successfully"
        }
    }

        @GetMapping("/create","/delete")
    fun getAllActivityCategories(@RequestHeader("Authorization") token: String): ActivityCategoryResponseDto{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        return ActivityCategoryResponseDto(activityCategoryService.getAllNames(user.id!!))
    }
}