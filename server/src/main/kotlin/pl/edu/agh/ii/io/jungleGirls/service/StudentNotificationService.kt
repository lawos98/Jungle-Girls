package pl.edu.agh.ii.io.jungleGirls.service
import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.dto.StudentScore
import pl.edu.agh.ii.io.jungleGirls.enum.StudentNotificationType
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.model.StudentNotification
import pl.edu.agh.ii.io.jungleGirls.repository.StudentNotificationRepository
import java.time.LocalDateTime
import java.util.*
@Service
class StudentNotificationService(
        private val studentNotificationRepository: StudentNotificationRepository
    ){

    fun generateStudentNotification(activity: Activity, studentScore: StudentScore, authorId:Long,studentNotificationType: StudentNotificationType){
        val studentNotification = StudentNotification(
            subject = studentNotificationType.getSubject(),
            content = studentNotificationType.getContent(activity.name,studentScore.value,activity.maxScore),
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
