package pl.edu.agh.ii.io.jungleGirls.service
import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.dto.CourseGroupNotificationRequest
import pl.edu.agh.ii.io.jungleGirls.dto.StudentNotificationRequest
import pl.edu.agh.ii.io.jungleGirls.dto.StudentNotificationStudentRequest
import pl.edu.agh.ii.io.jungleGirls.dto.StudentScore
import pl.edu.agh.ii.io.jungleGirls.enum.StudentNotificationType
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroup
import pl.edu.agh.ii.io.jungleGirls.model.StudentNotification
import pl.edu.agh.ii.io.jungleGirls.repository.StudentNotificationRepository
import pl.edu.agh.ii.io.jungleGirls.util.checkIsBlank
import java.time.LocalDateTime
import java.util.*
@Service
class StudentNotificationService(
        private val studentNotificationRepository: StudentNotificationRepository
    ){

    fun generateStudentNotification(activity: Activity, studentScore: StudentScore, authorId:Long,studentNotificationType: StudentNotificationType){
        studentNotificationRepository.save(studentNotificationType.getSubject(),studentNotificationType.getContent(activity.name,studentScore.value,activity.maxScore),LocalDateTime.now(),authorId,studentScore.id,false).block()
    }
    fun getAllStudentNotifications(studentId: Long):List<StudentNotification> {
        return studentNotificationRepository.findAllByStudentId(studentId).collectList().block() as ArrayList<StudentNotification>
    }

    fun updateWasRead(studentId: Long, studentNotificationId : Long):Either<String,StudentNotification> {
        val studentNotification = studentNotificationRepository.updateWasRead(studentId, studentNotificationId).block()
        return studentNotification?.right() ?: "No student notification found".left()
    }

     fun getStudents(instructorId:Long):List<StudentNotificationStudentRequest>{
        return studentNotificationRepository.getStudentsByInstructorId(instructorId).collectList().block() as ArrayList
    }

    private fun validateStudentIds(instructorId: Long,studentIds:List<Long>):Either<String, None>{
        if(studentIds.isEmpty()) return "Student list is empty!".left()
        return if (getStudents(instructorId).stream().map(StudentNotificationStudentRequest::id).toList().containsAll(studentIds)) None.right() else "Wrong student id!".left()
    }

    fun validateStudentNotification(instructorId: Long, studentNotificationRequest: StudentNotificationRequest): Either<String, None> {
        return validateStudentIds(instructorId,studentNotificationRequest.studentIds)
            .flatMap { _ -> checkIsBlank(studentNotificationRequest.subject, "Subject is empty!")
                .flatMap { _ -> checkIsBlank(studentNotificationRequest.content,"Content is empty!") }}


    }

    fun sendStudentsNotifications(instructorId: Long, studentNotificationRequest: StudentNotificationRequest) {
        for(studentId in studentNotificationRequest.studentIds){
            studentNotificationRepository.
            save(
                subject = studentNotificationRequest.subject,
                content = studentNotificationRequest.content,
                date = LocalDateTime.now(),
                authorId = instructorId,
                studentId = studentId,
                wasRead = false).block()
        }
    }
}
