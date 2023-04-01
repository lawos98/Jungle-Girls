package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.data.relational.core.mapping.Table
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroupActivity
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.repository.*
import java.time.LocalDateTime

@Service
class ActivityService(
    private val activityRepository: ActivityRepository,
    private val courseGroupActivityRepository: CourseGroupActivityRepository,
//    private val activityCategoryService: ActivityCategoryService,
//    private val activityTypeService: ActivityTypeService
    ){

    fun existsByName(name: String): Boolean {
        return activityRepository.existsByName(name).block() ?: false
    }
//    @Transactional
    fun createActivity(activity: Activity,groupIds:ArrayList<Long>):Either<String,None>{
        return validateActivity(activity).flatMap {_ ->
            @Transactional
            fun transaction(): Boolean {
                val createdActivity = activityRepository.save(activity).block() ?: return false
                for(groupId in groupIds){
                    courseGroupActivityRepository.save(CourseGroupActivity(groupId, createdActivity.id!!)).block() ?: return false
                }
                return true
            }
            if(!transaction()) return "Error while saving to database".left()

            return None.right()
        }
    }

    private fun checkIsBlank(value: String, errorMessage: String): Either<String, None> {
        if (value.isBlank()) {
            return errorMessage.left()
        }
        return None.right()
    }

    private fun checkIsStartDateBeforeEndDate(startDate: LocalDateTime, endDate: LocalDateTime): Either<String, None>{
        if(startDate.isAfter(endDate)){
            return "Activity can not start after it ends!".left()
        }
        return None.right()
    }
    private fun checkIsStartDateInTheFuture(startDate: LocalDateTime): Either<String, None>{
        if(startDate.isBefore(LocalDateTime.now().minusMinutes(1))){
            return "Activity can not start in the past!".left()
        }
        return None.right()
    }

    private fun checkIsMaxScorePositive(maxScore: Double) : Either<String, None>{
        return if(maxScore <= 0f)  "Max Score must be positive!".left() else None.right()
    }

//    private fun checkIfCategoryIdExists(id : Long): Either<String, None> {
//        return if(activityCategoryService.existsById(id)) None.right() else "Requested activity category does not exist!".left()
//    }
//    private fun checkIfTypeIdExists(id : Long): Either<String, None> {
//        return if(activityTypeService.existsById(id)) None.right() else "Requested type category does not exist!".left()
//    }
    private fun checkIfNameIsNotTaken(name: String):Either<String, None>{
        return if(existsByName(name)) "Activity name is already taken!".left() else None.right()
    }


    private fun validateActivity(activity: Activity): Either<String, None> {
        return checkIsBlank(activity.name,"name can not be empty")
            .flatMap {_ -> checkIfNameIsNotTaken(activity.name)
            .flatMap {_ -> checkIsBlank(activity.description,"description can not be empty")
            .flatMap {_ -> checkIsStartDateBeforeEndDate(activity.startDate,activity.endDate)
            .flatMap {_ -> checkIsStartDateInTheFuture(activity.startDate)
            .flatMap {_ -> checkIsMaxScorePositive(activity.maxScore)
//            .flatMap {_ -> checkIfCategoryIdExists(activity.activityCategoryId)
//            .flatMap {_ -> checkIfTypeIdExists(activity.activityTypeId)
//            }}
            }}}}}
    }
}