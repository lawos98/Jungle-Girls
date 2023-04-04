package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.data.relational.core.mapping.Table
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroupActivity
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.repository.*
import pl.edu.agh.ii.io.jungleGirls.util.checkIsBlank
import java.time.LocalDateTime

@Service
class ActivityService(
    private val activityRepository: ActivityRepository,
    private val courseGroupActivityRepository: CourseGroupActivityRepository,
    ){

    fun existsByName(name: String): Boolean {
        return activityRepository.existsByName(name).block() ?: false
    }
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
            }}}}}
    }
}