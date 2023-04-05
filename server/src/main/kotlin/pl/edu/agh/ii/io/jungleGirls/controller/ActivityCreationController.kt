package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityCreationResponseDto
import pl.edu.agh.ii.io.jungleGirls.dto.CreateActivityDto
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.service.*

@RestController
@RequestMapping("api/activity")
class ActivityCreationController(
    private val activityService: ActivityService,
    private val activityTypeService: ActivityTypeService,
    private val activityCategoryService: ActivityCategoryService,
    private val courseGroupService: CourseGroupService,
    private val tokenService: TokenService
) {

    @GetMapping("/create")
    fun getData(@RequestHeader("Authorization") token: String):ActivityCreationResponseDto{
        val user = tokenService.parseToken(token.substring("Bearer".length)) ?: throw InvalidBearerTokenException("Invalid token")

        return ActivityCreationResponseDto(
            groupNames = courseGroupService.getAllNamesById(user.roleId),
            activityTypeNames = activityTypeService.getAllNames(),
            activityCategoryNames = activityCategoryService.getAllNames(user.roleId)
        )
    }
    @PostMapping("/create")
    fun createActivity(@RequestBody payload:CreateActivityDto, @RequestHeader("Authorization") token: String): String{
        val user = tokenService.parseToken(token.substring("Bearer".length)) ?: throw InvalidBearerTokenException("Invalid token")

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
}