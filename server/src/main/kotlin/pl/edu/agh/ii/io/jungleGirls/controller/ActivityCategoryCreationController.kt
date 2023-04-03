package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.CreateActivityCategoryDto
import pl.edu.agh.ii.io.jungleGirls.dto.CreateActivityCategoryResponseDto
import pl.edu.agh.ii.io.jungleGirls.dto.CreateActivityDto
import pl.edu.agh.ii.io.jungleGirls.model.ActivityCategory
import pl.edu.agh.ii.io.jungleGirls.service.ActivityCategoryService

@RestController
@RequestMapping("api/activity_category")
class ActivityCategoryCreationController(
    private val activityCategoryService: ActivityCategoryService
) {

    @PostMapping("/create")
    fun createActivity(@RequestBody payload: CreateActivityCategoryDto): String {
        val activityCategory = ActivityCategory(
            name = payload.name,
            description = payload.description
        )

        when(val result = activityCategoryService.createCategory(activityCategory)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> return "Activity category created successfully"
        }
    }

    @GetMapping("/create")
    fun getAllActivityCategories(): CreateActivityCategoryResponseDto{
        return CreateActivityCategoryResponseDto(activityCategoryService.getAllNames())
    }
}