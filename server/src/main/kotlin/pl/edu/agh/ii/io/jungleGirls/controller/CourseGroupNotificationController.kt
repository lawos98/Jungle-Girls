package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.CourseGroupNotificationRequest
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroup
import pl.edu.agh.ii.io.jungleGirls.service.CourseGroupNotificationService
import pl.edu.agh.ii.io.jungleGirls.service.CourseGroupService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService

@RestController
@RequestMapping("api/group-notification")
class CourseGroupNotificationController (
    private val tokenService: TokenService,
    private val courseGroupService: CourseGroupService,
    private val courseGroupNotificationService: CourseGroupNotificationService,
){
    @GetMapping("/create")
    fun getGroups(@RequestHeader("Authorization") token: String):List<CourseGroup>{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        return courseGroupService.getAllGroups(user.id);
    }

    @PostMapping("/create")
    fun createGroupNotification(@RequestHeader("Authorization") token: String,@RequestBody payload: CourseGroupNotificationRequest){
        val user = tokenService.parseToken(token.substring("Bearer".length))
        val result = courseGroupNotificationService.validateCourseGroupNotification(user.id,payload)
        if(result is Either.Left){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, result.value)
        }
        courseGroupNotificationService.sendCourseGroupNotifications(user.id,payload)

    }

}

