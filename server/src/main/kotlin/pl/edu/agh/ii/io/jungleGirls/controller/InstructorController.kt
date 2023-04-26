package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.InstructorGroups
import pl.edu.agh.ii.io.jungleGirls.enum.Roles
import pl.edu.agh.ii.io.jungleGirls.service.CourseGroupService
import pl.edu.agh.ii.io.jungleGirls.service.RoleService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService

@RestController
@RequestMapping("/api/instructor")
class InstructorController(
    private val roleService: RoleService,
    private val tokenService: TokenService,
    private val courseGroupService: CourseGroupService
) {

    @GetMapping
    fun getCourseGroups(@RequestHeader("Authorization") token: String):InstructorGroups {
        val user = tokenService.parseToken(token.substring("Bearer".length))
        when(val role = roleService.getRoleByUserId(user.id)) {
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, role.value)
            is Either.Right -> {
                if (role.value.id!=Roles.COORDINATOR.getId() && role.value.id!=Roles.LECTURER.getId()) throw ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this resource")
                println("=====================================")
                println(user.id)
                println(courseGroupService.getAllGroups(user.id))
                return InstructorGroups(courseGroupService.getAllGroups(user.id).map{ it.id })
            }
        }
    }
}
