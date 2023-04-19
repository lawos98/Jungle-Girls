package pl.edu.agh.ii.io.jungleGirls.controller


import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.StudentNotificationResponse
import pl.edu.agh.ii.io.jungleGirls.service.LoginUserService
import pl.edu.agh.ii.io.jungleGirls.service.StudentNotificationService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService
import java.time.format.DateTimeFormatter


@RestController
@RequestMapping("api")
class StudentNotificationController(
        private val studentNotificationService: StudentNotificationService,
        private val tokenService: TokenService,
        private val loginUserService: LoginUserService
    ) {
    @GetMapping("/student_notification")
    fun getStudentNotifications(@RequestHeader("Authorization") token: String): List<StudentNotificationResponse> {
        val user = tokenService.parseToken(token.substring("Bearer".length))
        val studentNotificationList = studentNotificationService.getAllStudentNotifications(user.id!!)
        val authorNamesMap = HashMap<Long, String>()
        val studentNotificationResponseList = ArrayList<StudentNotificationResponse>()
        for(studentNotification in studentNotificationList){
            var authorName = authorNamesMap[studentNotification.authorId]
            if(authorName == null){
                authorName = when(val author = loginUserService.findByIndex(studentNotification.authorId)){
                    null -> "Unknown"
                    else -> "${author.firstname} ${author.lastname}"
                }
                authorNamesMap[studentNotification.authorId] = authorName
            }
            studentNotificationResponseList.add(
                StudentNotificationResponse(
                    id = studentNotification.id!!,
                    date = studentNotification.date.format(DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy")),
                    subject = studentNotification.subject,
                    content = studentNotification.content,
                    author = authorName,
                    wasRead = studentNotification.wasRead
                )
            )

        }
        return studentNotificationResponseList
    }

    @PutMapping("/student_notification")
    fun updateWasRead(@RequestHeader("Authorization") token: String,@RequestBody studentNotificationId:Long):String{
        val user = tokenService.parseToken(token.substring("Bearer".length))

        when (val result = studentNotificationService.updateWasRead(user.id!!,studentNotificationId)){
            is Either.Right ->{
                return "wasRead set to true successfully"
             }
            is Either.Left ->{
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,result.value)
            }
        }
    }
}