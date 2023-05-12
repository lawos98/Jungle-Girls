package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.dto.CourseGroupNotificationRequest
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroup
import pl.edu.agh.ii.io.jungleGirls.repository.StudentNotificationRepository
import pl.edu.agh.ii.io.jungleGirls.util.checkIsBlank
import java.time.LocalDateTime

@Service
class CourseGroupNotificationService(
    private val studentNotificationRepository: StudentNotificationRepository,
    private val courseGroupService : CourseGroupService,
) {

    private fun validateGroupIds(instructorId: Long,groupIds:List<Long>):Either<String, None>{
        if(groupIds.isEmpty()) return "group list is empty!".left()
        return if (courseGroupService.getAllGroups(instructorId).stream().map(CourseGroup::id).toList().containsAll(groupIds)) arrow.core.None.right() else "Wrong group id!".left()
    }

    fun validateCourseGroupNotification(instructorId: Long,courseGroupNotificationRequest: CourseGroupNotificationRequest): Either<String, None>{
        return validateGroupIds(instructorId,courseGroupNotificationRequest.groupIds)
            .flatMap { _ -> checkIsBlank(courseGroupNotificationRequest.subject, "Subject is empty!")
                .flatMap { _ -> checkIsBlank(courseGroupNotificationRequest.content,"Content is empty!") }}
    }

    fun sendCourseGroupNotifications(instructorId:Long,courseGroupNotificationRequest: CourseGroupNotificationRequest){
        for(groupId in courseGroupNotificationRequest.groupIds){
            for(student in courseGroupService.getAllStudentsByGroupId(groupId)){
                studentNotificationRepository.
                save(
                    subject = courseGroupNotificationRequest.subject,
                    content = courseGroupNotificationRequest.content,
                    date = LocalDateTime.now(),
                    authorId = instructorId,
                    studentId = student.id,
                    wasRead = false).block()
            }


        }

    }
}
