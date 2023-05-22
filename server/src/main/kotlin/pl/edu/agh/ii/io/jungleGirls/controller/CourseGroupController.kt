package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.CourseGroupRespone
import pl.edu.agh.ii.io.jungleGirls.dto.CreateCourseGroup
import pl.edu.agh.ii.io.jungleGirls.dto.SecretCode
import pl.edu.agh.ii.io.jungleGirls.enum.Permissions
import pl.edu.agh.ii.io.jungleGirls.enum.Roles
import pl.edu.agh.ii.io.jungleGirls.model.StudentDescription
import pl.edu.agh.ii.io.jungleGirls.service.CourseGroupService
import pl.edu.agh.ii.io.jungleGirls.service.RolePermissionService
import pl.edu.agh.ii.io.jungleGirls.service.RoleService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService

@RestController
@RequestMapping("/api/course-group")
class CourseGroupController(
    private val courseGroupService: CourseGroupService,
    private val tokenService: TokenService,
    private val rolePermissionService: RolePermissionService,
    private val roleService: RoleService
) {

    @GetMapping("/secret-code/{id}")
    fun generateNewSecretCode(@PathVariable id: Long,@RequestHeader("Authorization") token: String): SecretCode {
        val instructor = tokenService.parseToken(token.substring("Bearer".length))
        if (!rolePermissionService.checkUserPermission(instructor.id, Permissions.USERS_MANAGEMENT) && !courseGroupService.checkLecturerGroup(instructor.id, id)) throw ResponseStatusException(HttpStatus.FORBIDDEN,"You don't have permission to generate new secret code")
        if(!courseGroupService.checkLecturerGroup(instructor.id,id)){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Group does not belongs to instructor")
        }
        when(val group=courseGroupService.generateNewSecretCode(id)){
            is Either.Right ->{
                return group.value
            }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,group.value)
            }
        }
    }

    @PatchMapping("/secret-code")
    fun updateUserRoleViaSecretCode(@RequestBody payload:SecretCode,@RequestHeader("Authorization") token: String):StudentDescription{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        when(val role = roleService.getRoleByUserId(user.id)){
           is Either.Right ->{
               print(role)
               if(role.value.id!=Roles.UNAUTHORIZED.getId())throw ResponseStatusException(HttpStatus.FORBIDDEN,"You are not unauthorized user")
               when(val updatedUser=courseGroupService.updateGroupViaSecretCode(payload.code, user)){
                   is Either.Right ->{
                       return updatedUser.value
                   }
                   is Either.Left -> {
                       throw ResponseStatusException(HttpStatus.BAD_REQUEST,updatedUser.value)
                   }
               }
           }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,role.value)
            }
        }
    }

    @PutMapping
    fun createGroup(@RequestBody payload:CreateCourseGroup,@RequestHeader("Authorization") token: String):CourseGroupRespone{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        when(val role = roleService.getRoleByUserId(user.id)){
            is Either.Right ->{
                print(role)
                if(role.value.id!=Roles.COORDINATOR.getId() && role.value.id!=Roles.LECTURER.getId())throw ResponseStatusException(HttpStatus.FORBIDDEN,"You are not coordinator or lecturer")
                when(val courseGroup = courseGroupService.createCourseGroup(payload.name,user.id)){
                    is Either.Right ->{
                        return CourseGroupRespone(courseGroup.value.id,courseGroup.value.name,courseGroup.value.instructorId)
                    }
                    is Either.Left -> {
                        throw ResponseStatusException(HttpStatus.BAD_REQUEST,courseGroup.value)
                    }
                }
            }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,role.value)
            }
        }
    }
}
