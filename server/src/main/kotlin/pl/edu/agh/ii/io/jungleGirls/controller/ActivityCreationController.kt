package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.CreateActivityDto
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.service.ActivityCategoryService
import pl.edu.agh.ii.io.jungleGirls.service.ActivityService
import pl.edu.agh.ii.io.jungleGirls.service.ActivityTypeService
import pl.edu.agh.ii.io.jungleGirls.service.CourseGroupService

@RestController
@RequestMapping("api/activity")
class ActivityCreationController(
    private val activityService: ActivityService,
    private val activityTypeService: ActivityTypeService,
    private val activityCategoryService: ActivityCategoryService,
    private val courseGroupService: CourseGroupService,
) {

    @GetMapping("/create")
    fun getData(){

    }
    @PostMapping("/create")
    fun createActivity(@RequestBody payload:CreateActivityDto): String{
        val courseIds = courseGroupService.getIds(payload.courseGroupNames)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested group does not exist!");
        val activityTypeId = activityTypeService.getIdByName(payload.activityTypeName)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested activity type does not exist!");
        val activityCategoryId = activityCategoryService.getIdByName(payload.activityCategoryName)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Requested activity category does not exist!")



        val activity = Activity(
            name = payload.name,
            description = payload.description,
            startDate = payload.startDate,
            endDate = payload.endDate,
            maxScore = payload.maxScore,
            activityTypeId = activityTypeId,
            activityCategoryId = activityCategoryId
        )
        when(val result = activityService.createActivity(activity,courseIds)){
            is Either.Right -> {
                return "Activity created successfully"
            }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            }
        }



    }
}