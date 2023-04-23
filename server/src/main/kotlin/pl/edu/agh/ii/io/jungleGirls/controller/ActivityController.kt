package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.*
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.service.*
import java.time.LocalDateTime

@RestController
@RequestMapping("api/activity")
class ActivityController(
    private val activityService: ActivityService,
    private val activityTypeService: ActivityTypeService,
    private val activityCategoryService: ActivityCategoryService,
    private val courseGroupService: CourseGroupService,
    private val tokenService: TokenService,
    private val courseGroupActivityService: CourseGroupActivityService,
) {

//    @GetMapping()
//    fun getActivities(@RequestHeader("Authorization") token: String):List<Activity>{
//        val user = tokenService.parseToken(token.substring("Bearer".length))
//        return activityService.getAllActivityByInstructorId(user.id!!)
//    }
    @GetMapping()
    fun getActivities(@RequestHeader("Authorization") token: String):List<ActivityRequest>{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        val activities = activityService.getAllActivityByInstructorId(user.id!!)
        val result = ArrayList<ActivityRequest>()
        for(activity in activities){
            val courseGroupActivities = courseGroupActivityService.getAllByActivityId(activity.id!!)
            val courseGroupNames  = ArrayList<String>()
            val courseGroupStartDates  = ArrayList<LocalDateTime>()
            for(courseGroupActivity in courseGroupActivities){
                courseGroupNames.add(courseGroupService.getNameById(courseGroupActivity.courseGroupId)!!)
                courseGroupStartDates.add(courseGroupActivity.startDate)
            }

            result.add(
                ActivityRequest(
                    id = activity.id!!,
                    name = activity.name,
                    description = activity.description,
                    duration = activity.duration,
                    maxScore = activity.maxScore,
                    courseGroupNames = courseGroupNames,
                    courseGroupStartDates = courseGroupStartDates,
                    activityTypeName = activityTypeService.getNameById(activity.activityTypeId)!!,
                    activityCategoryName = activityCategoryService.getNameByIdAndInstructorId(activity.activityCategoryId,user.id)!!
                )
            )
        }
        return result
    }

    @GetMapping("/create","/edit/{*}")
    fun getCreationData(@RequestHeader("Authorization") token: String):ActivityCreationResponse{
        val user = tokenService.parseToken(token.substring("Bearer".length))

        return ActivityCreationResponse(
            groupNames = courseGroupService.getAllNamesById(user.id!!),
            activityTypeNames = activityTypeService.getAllNames(),
            activityCategoryNames = activityCategoryService.getAllNames(user.id),
            activityNames = activityService.getAllNames(user.id)
        )
    }

    @PutMapping("/edit/{id}")
    fun editActivity(@RequestHeader("Authorization") token: String, @PathVariable id: Long, @RequestBody payload:CreateActivityRequest):String{
        val user = tokenService.parseToken(token.substring("Bearer".length))

        val activityToEdit = activityService.findByIdAndInstructorId(id,user.id!!)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity does not exist")

        val courseGroupIds = when(val result = courseGroupService.validateNames(user.id,payload.courseGroupNames)){
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

        val editedActivity = Activity(
            name = payload.name,
            maxScore = payload.maxScore,
            duration = payload.duration,
            description = payload.description,
            activityTypeId = activityTypeId,
            activityCategoryId = activityCategoryId
        )

        return when(val result = activityService.editActivity(user.id,activityToEdit,editedActivity,courseGroupIds,payload.courseGroupStartDates)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> "Activity edited successfully"
        }
    }

    @PutMapping("/delete/{id}")
    fun deleteActivity(@RequestHeader("Authorization") token: String, @PathVariable id: Long):String{
        val user = tokenService.parseToken(token.substring("Bearer".length))

        val activityToDelete = activityService.findByIdAndInstructorId(id,user.id!!)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity does not exist")

        return when(val result = activityService.deleteActivity(activityToDelete)){
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
            is Either.Right -> "Activity category deleted successfully"
        }
    }
    @PostMapping("/create")
    fun createActivity(@RequestBody payload:CreateActivityRequest, @RequestHeader("Authorization") token: String): String{
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

//    @PostMapping("/delete")
//    fun deleteActivity(@RequestBody payload: DeleteActivityRequest, @RequestHeader("Authorization") token: String): String {
//        val user = tokenService.parseToken(token.substring("Bearer".length))
//
//        val activityToDelete = activityService.findByInstructorIdAndName(user.id!!,payload.name)
//            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity does not exist")
//
//        return when(val result = activityService.deleteActivity(activityToDelete)){
//            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
//            is Either.Right -> "Activity category deleted successfully"
//        }
//    }
}
