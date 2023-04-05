package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroupActivity
import pl.edu.agh.ii.io.jungleGirls.repository.*
import pl.edu.agh.ii.io.jungleGirls.util.checkIsBlank
import java.time.Duration
import java.time.LocalDateTime

@Service
class ActivityService(
    private val activityRepository: ActivityRepository,
    private val courseGroupActivityRepository: CourseGroupActivityRepository,
    ){

    fun existsByInstructorIdAndName(instructorId:Long, name: String): Boolean {
        return activityRepository.existsByInstructorIdAndName(instructorId,name).block() ?: false
    }
    fun createActivity(instructorId: Long,activity: Activity,groupIds:ArrayList<Long>,courseGroupStartDates:ArrayList<LocalDateTime>):Either<String,None>{
        return validateActivity(instructorId,activity).flatMap {_ ->
            checkIsCorrectSize(courseGroupStartDates,groupIds).flatMap { _->
            validateStartDates(courseGroupStartDates).flatMap { _->

            @Transactional
            fun transaction(): Boolean {
                val createdActivity = activityRepository.save(activity).block() ?: return false
                for((groupId,startDime) in groupIds.zip(courseGroupStartDates)){
                    courseGroupActivityRepository.save(CourseGroupActivity(groupId, createdActivity.id!!,startDime)).block() ?: return false
                }
                return true
            }
            if(!transaction()) return "Error while saving to database".left()

            return None.right()
        }}}
    }


    private fun checkIsMaxScorePositive(maxScore: Double) : Either<String, None>{
        return if(maxScore <= 0f)  "Max Score must be positive!".left() else None.right()
    }
    private fun checkIfNameIsNotTaken(instructorId:Long, name: String):Either<String, None>{
        return if(existsByInstructorIdAndName(instructorId,name)) "Activity name is already taken!".left() else None.right()
    }
    private fun checkIsDurationPositive(duration : Duration):Either<String, None>{
        return if(duration.isNegative || duration.isZero) "Duration must be positive!".left() else None.right()
    }



    private fun validateActivity(instructorId: Long,activity: Activity): Either<String, None> {
        return checkIsBlank(activity.name,"name can not be empty")
            .flatMap {_ -> checkIfNameIsNotTaken(instructorId,activity.name)
            .flatMap {_ -> checkIsBlank(activity.description,"description can not be empty")
            .flatMap {_ -> checkIsDurationPositive(activity.duration)
            .flatMap {_ -> checkIsMaxScorePositive(activity.maxScore)
            }}}}
    }
    private fun checkIsCorrectSize(a: ArrayList<LocalDateTime>, b: ArrayList<Long>):Either<String, None>{
        return if(a.size != b.size) "Incorrect number of start dates!".left() else None.right()
    }

    private fun validateStartDates(courseGroupStartDates: ArrayList<LocalDateTime>): Either<String, None>{
        return if(courseGroupStartDates.any{LocalDateTime.now().minusMinutes(1).isAfter(it)}) "Activity can not start in the past!".left()
        else None.right()

    }
}