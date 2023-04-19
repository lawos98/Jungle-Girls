package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityScoreList
import pl.edu.agh.ii.io.jungleGirls.dto.StudentScore
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityScore
import pl.edu.agh.ii.io.jungleGirls.enum.Permissions
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.model.Score
import pl.edu.agh.ii.io.jungleGirls.repository.ScoreRepository

@Service
class ScoreService(
    private val rolePermissionService: RolePermissionService,
    private val courseGroupService: CourseGroupService,
    private val scoreRepository: ScoreRepository,
    private val activityService: ActivityService,
    private val studentDescriptionService: StudentDescriptionService
) {

    fun getScores(groupId:Long, lecturerId:Long):Either<String,List<ActivityScoreList>>{
        if(!courseGroupService.existsByCourseId(groupId)) return "That course group is not accessible".left()
        if(!rolePermissionService.checkUserPermission(lecturerId,Permissions.GRADE_VIEW) && !courseGroupService.checkLecturerGroup(lecturerId,groupId)){
            return "You don't have permission to view scores".left()
        }
        val scores = scoreRepository.getScoresByGroupId(groupId).collectList().block() ?: return "Server cannot find scores for groupId $groupId".left()
        val studentList = courseGroupService.getAllStudentsByGroupId(groupId).map { StudentScore(it.id ?: return "Missing Id for user".left(),it.username,it.firstname,it.lastname,null) }
        return activityService.getAllActivityByGroupId(groupId).distinct().map { activity ->
            val currentScoreList = scores.filter {it.activityId==activity.id }
            val studentHavingScoreList = currentScoreList.map {elem->
                val student = studentList.find{it.id==elem.studentId} ?: return "Server cannot find user ${elem.studentId} on table Score".left()
                student.value=elem.value
                student
            }
            val studentNotHavingScoreList = studentList.filterNot { student -> studentHavingScoreList.map { it.id }.contains(student.id) }
            ActivityScoreList(activity, (studentHavingScoreList+studentNotHavingScoreList).sortedBy { it.id })
        }.toCollection(ArrayList()).right()
    }
    fun getScore(user: LoginUser): Either<String, List<ActivityScore>> {
        val student = studentDescriptionService.findById(user.id!!) ?: return "User is not a student".left()
        val groupId = student.courseGroupId ?: return "Student is not in any course group".left()
        return activityService.getAllActivityByGroupId(groupId).map { activity ->
            val activityId=activity.id ?: return "Server cannot find activity Id".left()
            ActivityScore(activity,scoreRepository.getScore(activityId,student.id,groupId).block()?.value)
        }.right()
    }


    fun updateScores(groupId: Long, lecturerId: Long, scoreList: List<ActivityScoreList>): Either<String, List<ActivityScoreList>> {
        if (!courseGroupService.existsByCourseId(groupId)) return "That course group is not accessible".left()
        if (!rolePermissionService.checkUserPermission(lecturerId, Permissions.GRADE_EDIT) && !courseGroupService.checkLecturerGroup(lecturerId, groupId)) return "You don't have permission to view scores".left()
        scoreList.forEach { elem ->
            val activity = elem.activity
            if (activity != activityService.getById(activity.id!!)) return "Activity with id ${activity.id} has incorrect values".left()
            if (!activityService.getAllActivityByGroupId(groupId).map { it.id }.contains(activity.id)) return "Activity with that id dont belong to this course group".left()
            elem.students.forEach { student ->
                if(!courseGroupService.getAllStudentsByGroupId(groupId).map { it.id }.contains(student.id)) return "Student with id ${student.id} dont belong to this course group with id $groupId".left()
                val newValue = student.value
                val oldScore = scoreRepository.getScore(activity.id!!,student.id,groupId).block()
                val oldValue = oldScore?.value
                when {
                    newValue == null && oldValue != null -> scoreRepository.deleteById(oldScore.id!!).block()
                    newValue != null && (newValue > activity.maxScore || newValue < 0) -> return "Score must be between 0 and max score ${activity.maxScore} | new Value : $newValue".left()
                    newValue != null && oldValue == null -> scoreRepository.save(Score(studentId = student.id, activityId = activity.id!!, value = newValue)).block()
                    newValue != null && newValue != oldValue -> if (oldScore != null) {
                        scoreRepository.updateScoreById(oldScore.id!!, newValue).block() ?: "Server cannot update score for activity Id : ${activity.id} and student Id :$student.id".left()
                    }
                }}
            }
        return scoreList.right()
        }

}