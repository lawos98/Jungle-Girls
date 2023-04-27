package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.agh.ii.io.jungleGirls.dto.CreateActivityRequest
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

    fun existsById(id: Long):Boolean{
        return activityRepository.existsById(id).block() ?: false
    }

    fun existsByInstructorIdAndName(instructorId:Long, name: String): Boolean {
        return activityRepository.existsByInstructorIdAndName(instructorId,name).block() ?: false
    }
    fun createActivity(instructorId: Long, createActivity: CreateActivityRequest,activityTypeId: Long,activityCategoryId: Long,groupIds:ArrayList<Long>):Either<String,None>{
        return validateActivity(instructorId,createActivity.name,createActivity.description,createActivity.duration,createActivity.maxScore).flatMap {_ ->
            checkIsCorrectSize(createActivity.courseGroupStartDates,groupIds).flatMap { _->
            validateStartDates(createActivity.courseGroupStartDates).flatMap { _->

            @Transactional
            fun transaction(): Boolean {
                val createdActivity = activityRepository.save(createActivity.name,createActivity.maxScore,createActivity.duration,createActivity.description,activityTypeId,activityCategoryId).block() ?: return false
                for((groupId,startDime) in groupIds.zip(createActivity.courseGroupStartDates)){
                    courseGroupActivityRepository.save(CourseGroupActivity(groupId, createdActivity.id,startDime)).block() ?: return false
                }
                return true
            }
            if(!transaction()) return "Error while saving to database".left()

            return None.right()
        }}}
    }
    fun editActivity(instructorId: Long, activityToEdit : Activity, createActivity: CreateActivityRequest, activityTypeId:Long, activityCategoryId:Long, groupIds:ArrayList<Long>): Either<String, None> {
        return validateEditedActivity(instructorId,createActivity.name,createActivity.maxScore,createActivity.description,createActivity.duration,activityToEdit).flatMap {_ ->
            checkIsCorrectSize(createActivity.courseGroupStartDates,groupIds).flatMap { _->
                validateStartDates(createActivity.courseGroupStartDates).flatMap { _->


                    @Transactional
                    fun transaction(): Boolean {
                        val edited = activityRepository.editActivity(activityToEdit.id,createActivity.name,createActivity.maxScore,createActivity.description,createActivity.duration,activityTypeId,activityCategoryId).block()
                            ?: return false
                        for((groupId,startDime) in groupIds.zip(createActivity.courseGroupStartDates)){
                            courseGroupActivityRepository.updateStartDate(groupId, edited.id,startDime).block() ?: return false
                        }
                        return true
                    }
                    if(!transaction()) return "Error while saving to database".left()

                    return None.right()
        }}}
    }

    private fun validateEditedActivity(instructorId: Long, name: String,maxScore: Double,description:String,duration:Duration, activityToEdit: Activity): Either<String, None> {
        return checkIsBlank(name,"name can not be empty")
            .flatMap {_ ->  checkIfEditedNameIsNotTaken(instructorId,name,activityToEdit.name)
                .flatMap {_ -> checkIsBlank(description,"description can not be empty")
                    .flatMap {_ -> checkIsDurationPositive(duration)
                        .flatMap {_ -> checkIsMaxScorePositive(maxScore)
                        }}}}
    }


    private fun checkIsMaxScorePositive(maxScore: Double) : Either<String, None>{
        return if(maxScore <= 0f)  "Max Score must be positive!".left() else None.right()
    }
    private fun checkIfNameIsNotTaken(instructorId:Long, name: String):Either<String, None>{
        return if(existsByInstructorIdAndName(instructorId,name)) "Activity name is already taken!".left() else None.right()
    }
    private fun checkIfEditedNameIsNotTaken(instructorId:Long, newName: String,oldName: String):Either<String, None>{
        return if(oldName != newName && existsByInstructorIdAndName(instructorId,newName)) "Activity name is already taken!".left() else None.right()
    }
    private fun checkIsDurationPositive(duration : Duration):Either<String, None>{
        return if(duration.isNegative || duration.isZero) "Duration must be positive!".left() else None.right()
    }



    private fun validateActivity(instructorId: Long,name: String,description: String,duration: Duration,maxScore: Double): Either<String, None> {
        return checkIsBlank(name,"name can not be empty")
            .flatMap {_ -> checkIfNameIsNotTaken(instructorId,name)
            .flatMap {_ -> checkIsBlank(description,"description can not be empty")
            .flatMap {_ -> checkIsDurationPositive(duration)
            .flatMap {_ -> checkIsMaxScorePositive(maxScore)
            }}}}
    }
    private fun checkIsCorrectSize(a: ArrayList<LocalDateTime>, b: ArrayList<Long>):Either<String, None>{
        return if(a.size != b.size) "Incorrect number of start dates!".left() else None.right()
    }

    private fun validateStartDates(courseGroupStartDates: ArrayList<LocalDateTime>): Either<String, None>{
        return if(courseGroupStartDates.any{LocalDateTime.now().minusMinutes(1).isAfter(it)}) "Activity can not start in the past!".left()
        else None.right()

    }

    fun getAllNames(instructorId: Long): java.util.ArrayList<String> {
        return activityRepository.getNamesByInstructorId(instructorId).collectList().block() as ArrayList<String>
    }



    fun findByInstructorIdAndName(instructorId: Long, name: String): Activity? {
        return activityRepository.findByInstructorIdAndName(instructorId,name).block()
    }

    fun findByIdAndInstructorId(activityId: Long, instructorId: Long): Activity? {
        return activityRepository.findByIdAndInstructorId(activityId,instructorId).block()
    }


    fun deleteActivity(activity: Activity): Either<String, None>{
        return checkIfCanBeDeleted(activity.id).flatMap { _ ->
            @Transactional
            fun transaction(){
                courseGroupActivityRepository.deleteByActivityId(activity.id).blockLast()
                activityRepository.delete(activity).block()
            }
            transaction()

            return None.right()
        }
    }
    private fun canBeDeleted(id: Long): Boolean{
        return activityRepository.canBeDeleted(id).block() ?: false
    }


    private fun checkIfCanBeDeleted(id: Long): Either<String, Long>{
        return if(canBeDeleted(id)) id.right() else "There are scores belonging to this activity!".left()
    }

    fun getAllActivityByGroupId(groupId: Long): ArrayList<Activity> {
        return activityRepository.getAllByGroupId(groupId).collectList().block() as ArrayList<Activity>
    }

    fun getById(id: Long): Activity?{
        return activityRepository.findById(id).block()
    }

    fun getAllActivityByInstructorId(instructorId: Long): List<Activity> {
        return activityRepository.getAllActivityByInstructorId(instructorId).collectList().block() as ArrayList<Activity>
    }




}
