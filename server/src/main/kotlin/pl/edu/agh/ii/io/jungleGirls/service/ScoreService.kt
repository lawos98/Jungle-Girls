package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityScoreList
import pl.edu.agh.ii.io.jungleGirls.dto.StudentScore
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityScore
import pl.edu.agh.ii.io.jungleGirls.enum.Permissions
import pl.edu.agh.ii.io.jungleGirls.enum.StudentNotificationType
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.repository.ScoreRepository
import java.io.BufferedWriter

@Service
class ScoreService(
    private val rolePermissionService: RolePermissionService,
    private val courseGroupService: CourseGroupService,
    private val scoreRepository: ScoreRepository,
    private val activityService: ActivityService,
    private val studentDescriptionService: StudentDescriptionService,
    private val studentNotificationService: StudentNotificationService,
    private val gitHubService: GitHubService
) {

    fun getScores(groupId: Long, lecturerId: Long): Either<String, List<ActivityScoreList>> {
        if (!courseGroupService.existsByCourseId(groupId)) return "That course group is not accessible".left()
        if (!rolePermissionService.checkUserPermission(lecturerId, Permissions.GRADE_VIEW) && !courseGroupService.checkLecturerGroup(lecturerId, groupId)) {
            return "You don't have permission to view scores".left()
        }
        val scores = scoreRepository.getScoresByGroupId(groupId).collectList().block() ?: return "Server cannot find scores for groupId $groupId".left()
        val studentList = courseGroupService.getAllStudentsByGroupId(groupId).map { StudentScore(it.id, it.username, it.firstname, it.lastname, null,null,null) }
        val studentMap = studentList.associateBy { it.id }
        val studentDescriptionMap = studentDescriptionService.getMapOfStudentDescription()

        return activityService.getAllActivityByGroupId(groupId).distinct().map { activity ->
            val currentScoreList = scores.filter { it.activityId == activity.id }
            val studentHavingScoreList = currentScoreList.mapNotNull { elem ->
                val student = studentMap[elem.studentId] ?: return "Server cannot find user ${elem.studentId} on table Score".left()
                StudentScore(student.id, student.username, student.firstname, student.lastname, elem.value,null,null)
            }
            val studentNotHavingScoreList = studentList.filterNot { student -> studentHavingScoreList.map { it.id }.contains(student.id) }
            ActivityScoreList(activity, (studentHavingScoreList + studentNotHavingScoreList).map { activityScore ->
                val studentDescription = studentDescriptionMap[activityScore.id] ?: return "Server cannot find student description for student ${activityScore.id}".left()
                val (lastCommitTime,lastCommitError)=gitHubService.getLastCommit(studentDescription,activity.name)
                activityScore.copy(lastCommitTime = lastCommitTime,lastCommitError = lastCommitError)
            }.sortedBy { it.id })
        }.toCollection(ArrayList()).right()
    }
    fun getScore(user: LoginUser): Either<String, List<ActivityScore>> {
        val student = studentDescriptionService.findById(user.id) ?: return "User is not a student".left()
        val groupId = student.courseGroupId ?: return "Student is not in any course group".left()
        return activityService.getAllActivityByGroupId(groupId).map { activity ->
            val activityId= activity.id
            ActivityScore(activity,scoreRepository.getScore(activityId,student.id,groupId).block()?.value)
        }.right()
    }


    fun updateScores(groupId: Long, lecturerId: Long, scoreList: List<ActivityScoreList>): Either<String, List<ActivityScoreList>> {
        if (!courseGroupService.existsByCourseId(groupId)) return "That course group is not accessible".left()
        if (!rolePermissionService.checkUserPermission(lecturerId, Permissions.GRADE_EDIT) && !courseGroupService.checkLecturerGroup(lecturerId, groupId)) return "You don't have permission to view scores".left()

        val activities= activityService.getAllActivityByGroupId(groupId).map { it.id }.toSet()
        val students= courseGroupService.getAllStudentsByGroupId(groupId).map { it.id }.toSet()
        val scoreMap = students.associate{ studentId ->
            (studentId to (scoreRepository.getScoreByStudentId(studentId).collectList().block()?.associateBy { score -> score?.activityId ?: return "Server error cant find score for student".left()}
                ?: return "Server error cant find scores for student".left()))
        }

        scoreList.forEach { elem ->
            val activity = elem.activity
            if (activity != activityService.getById(activity.id)) return "Activity with id ${activity.id} has incorrect values".left()
            if (!activities.contains(activity.id)) return "Activity with that id dont belong to this course group".left()
            elem.students.forEach { student ->
                if(!students.contains(student.id)) return "Student with id ${student.id} dont belong to this course group with id $groupId".left()
                val newValue = student.value
                val oldScore = scoreMap[student.id]?.get(activity.id)
                val oldValue = oldScore?.value
                when {
                    newValue == null && oldValue != null -> {
                        scoreRepository.deleteById(oldScore.id).block()
                        studentNotificationService.generateStudentNotification(activity,student,lecturerId,StudentNotificationType.DELETED_SCORE)
                    }
                    newValue != null && (newValue > activity.maxScore || newValue < 0) -> return "Score must be between 0 and max score ${activity.maxScore} | new Value : $newValue".left()
                    newValue != null && oldValue == null -> {
                        scoreRepository.save(student.id, activity.id,newValue).block()
                        studentNotificationService.generateStudentNotification(activity,student,lecturerId,StudentNotificationType.NEW_SCORE)
                    }
                    newValue != null && newValue != oldValue -> if (oldScore != null) {
                        scoreRepository.updateScoreById(oldScore.id, newValue).block() ?: "Server cannot update score for activity Id : ${activity.id} and student Id :$student.id".left()
                        studentNotificationService.generateStudentNotification(activity,student,lecturerId,StudentNotificationType.CHANGED_SCORE)
                    }
                }}
            }
        return scoreList.right()
    }

    fun generateCSV(instructorId: Long, groupId: Long, bufferedWriter: BufferedWriter) {

        val students = courseGroupService.getAllStudentIdsAndNamesByGroupId(groupId)
        val activities = courseGroupService.getAllActivityIdsAndNames(groupId)
        val scores = courseGroupService.getAllScoresWithActivityIdAndStudentIdByGroupId(groupId)

        for(studentName in students.values){
            bufferedWriter.write(",$studentName")
        }
        bufferedWriter.write("\n")
        for(activityEntry in activities.entries){
            bufferedWriter.write(activityEntry.value)
            for(studentEntry in students.entries){
                val score = scores[Pair(activityEntry.key,studentEntry.key)]
                bufferedWriter.write(",$score")
            }
            bufferedWriter.write("\n")
        }

    }

}
