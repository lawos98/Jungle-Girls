package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.*
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.service.*

@RestController
@RequestMapping("api/activity")
class ActivityController(
    private val activityService: ActivityService,
    private val activityTypeService: ActivityTypeService,
    private val activityCategoryService: ActivityCategoryService,
    private val courseGroupService: CourseGroupService,
    private val tokenService: TokenService
) {

    @GetMapping("/create")
    fun getCreationData(@RequestHeader("Authorization") token: String):ActivityCreationResponseDto{
        val user = tokenService.parseToken(token.substring("Bearer".length))

        return ActivityCreationResponseDto(
            groupNames = courseGroupService.getAllNamesById(user.id!!),
            activityTypeNames = activityTypeService.getAllNames(),
            activityCategoryNames = activityCategoryService.getAllNames(user.id),
            activityNames = activityService.getAllNames(user.id)
        )
    }
    @GetMapping("/delete")
    fun getActivities(@RequestHeader("Authorization") token: String):ActivityDeletionResponseDto{
        val user = tokenService.parseToken(token.substring("Bearer".length))

        return ActivityDeletionResponseDto(
            activityNames = activityService.getAllNames(user.id!!)
        )
    }
    @PostMapping("/create")
    fun createActivity(@RequestBody payload:CreateActivityDto, @RequestHeader("Authorization") token: String): String{
        val user = tokenService.parseToken(token.substring("Bearer".length))

        val courseGroupIds = when(val result = courseGroupService.validateNames(user.id!!,payload.courseGroupNames)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> result.value
        }
        val activityCategoryId = when(val result = activityCategoryService.validateName(user.id,payload.activityCategoryName)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> result.value
        }

        val activityTypeId = when(val result = activityTypeService.validateName(payload.activityTypeName)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> result.value
        }

        val activity = Activity(
            name = payload.name,
            description = payload.description,
            duration = payload.duration,
            maxScore = payload.maxScore,
            activityTypeId = activityTypeId,
            activityCategoryId = activityCategoryId
        )
        when(val result = activityService.createActivity(user.id,activity,courseGroupIds,payload.courseGroupStartDates)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> return "Activity created successfully"
        }
    }

    @PostMapping("/delete")
    fun deleteActivity(@RequestBody payload: DeleteActivityDto, @RequestHeader("Authorization") token: String): String {
        val user = tokenService.parseToken(token.substring("Bearer".length))

        val activityToDelete = activityService.findByInstructorIdAndName(user.id!!,payload.name)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity does not exist")

        return when(val result = activityService.deleteActivity(activityToDelete)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> "Activity category deleted successfully"
        }
    }
}