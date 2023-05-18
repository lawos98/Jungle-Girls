package pl.edu.agh.ii.io.jungleGirls.controller


import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.StudentNotificationRequest
import pl.edu.agh.ii.io.jungleGirls.dto.StudentNotificationResponse
import pl.edu.agh.ii.io.jungleGirls.dto.StudentNotificationStudentRequest
import pl.edu.agh.ii.io.jungleGirls.model.StudentNotification
import pl.edu.agh.ii.io.jungleGirls.repository.StudentDescriptionRepository
import pl.edu.agh.ii.io.jungleGirls.repository.StudentNotificationRepository
import pl.edu.agh.ii.io.jungleGirls.service.CourseGroupService
import pl.edu.agh.ii.io.jungleGirls.service.LoginUserService
import pl.edu.agh.ii.io.jungleGirls.service.StudentNotificationService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService
import java.time.format.DateTimeFormatter


@RestController
@RequestMapping("api/student-notification")
class StudentNotificationController(
        private val studentNotificationService: StudentNotificationService,
        private val tokenService: TokenService,
        private val loginUserService: LoginUserService,
        private val courseGroupService: CourseGroupService,
    ) {
    @GetMapping
    fun getStudentNotifications(@RequestHeader("Authorization") token: String): List<StudentNotificationResponse> {
        val user = tokenService.parseToken(token.substring("Bearer".length))
        return studentNotificationService.getAllStudentNotifications(user.id).map{ createStudentNotificationResponse(it)}
    }
    private fun createStudentNotificationResponse(studentNotification: StudentNotification):StudentNotificationResponse{
        val authorName = when(val author = loginUserService.findByIndex(studentNotification.authorId)){
            null -> "Unknown"
            else -> "${author.firstname} ${author.lastname}"
        }
        return StudentNotificationResponse(
            id = studentNotification.id,
            date = studentNotification.date.format(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy")),
            subject = studentNotification.subject,
            content = studentNotification.content,
            author = authorName,
            wasRead = studentNotification.wasRead
        )
    }

    @PutMapping("update/{id}")
    fun updateWasRead(@RequestHeader("Authorization") token: String,@PathVariable id: Long):StudentNotificationResponse{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        when (val result = studentNotificationService.updateWasRead(user.id,id)){
            is Either.Right ->{
                return createStudentNotificationResponse(result.value)
             }
            is Either.Left ->{
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,result.value)
            }
        }
    }


    @GetMapping("/create")
    fun getStudents(@RequestHeader("Authorization") token: String):List<StudentNotificationStudentRequest>{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        return studentNotificationService.getStudents(user.id)
    }

    @PostMapping("/create")
    fun sendStudentNotification(@RequestHeader("Authorization") token: String, @RequestBody payload:StudentNotificationRequest){
        val user = tokenService.parseToken(token.substring("Bearer".length))
        val result = studentNotificationService.validateStudentNotification(user.id,payload)
        if(result is Either.Left){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
        }
        studentNotificationService.sendStudentsNotifications(user.id,payload)
    }
}
