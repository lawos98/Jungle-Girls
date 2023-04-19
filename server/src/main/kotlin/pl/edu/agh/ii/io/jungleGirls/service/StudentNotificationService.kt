package pl.edu.agh.ii.io.jungleGirls.service
import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.dto.StudentScore
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.model.StudentNotification
import pl.edu.agh.ii.io.jungleGirls.repository.StudentNotificationRepository
import java.time.LocalDateTime
import java.util.*
@Service
class StudentNotificationService(
        private val studentNotificationRepository: StudentNotificationRepository
    ){

    fun generateNewScoreNotification(activity: Activity, studentScore: StudentScore, authorId:Long){
        val studentNotification = StudentNotification(
            subject = "New score notification.",
            content = "You received ${studentScore.value}/${activity.maxScore} points for the activity \"${activity.name}\".",
            date = LocalDateTime.now(),
            authorId = authorId,
            studentId = studentScore.id,
            wasRead = false
        )
        studentNotificationRepository.save(studentNotification).block()
    }
    fun generateScoreChangedNotification(activity: Activity, studentScore: StudentScore, authorId:Long,oldValue:Double){
        val studentNotification = StudentNotification(
            subject = "Score changed notification.",
            content = "Your score for the activity \"${activity.name}\" was changed from $oldValue to ${studentScore.value} points .",
            date = LocalDateTime.now(),
            authorId = authorId,
            studentId = studentScore.id,
            wasRead = false
        )
        studentNotificationRepository.save(studentNotification).block()
    }
    fun generateScoreDeletedNotification(activity: Activity, studentScore: StudentScore, authorId:Long){
        val studentNotification = StudentNotification(
            subject = "Score deleted notification.",
            content = "Your score received for the activity \"${activity.name}\" was deleted.",
            date = LocalDateTime.now(),
            authorId = authorId,
            studentId = studentScore.id,
            wasRead = false
        )
        studentNotificationRepository.save(studentNotification).block()
    }

    fun getAllStudentNotifications(studentId: Long):List<StudentNotification> {
        return studentNotificationRepository.findAllByStudentId(studentId).collectList().block() as ArrayList<StudentNotification>
    }

    fun updateWasRead(studentId: Long, studentNotificationId : Long):Either<String,StudentNotification> {
        val studentNotification = studentNotificationRepository.updateWasRead(studentId, studentNotificationId).block()
        return studentNotification?.right() ?: "No student notification found".left()
    }
}
