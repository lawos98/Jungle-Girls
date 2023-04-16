package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.*
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.service.RoleService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService

@RestController
@RequestMapping("/api/role")
class RoleController(
    private val roleService: RoleService,
    private val tokenService: TokenService
) {
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long):RoleResponse{
        when(val role = roleService.getRoleById(id)){
            is Either.Right -> return RoleResponse(role.value.id!!,role.value.name,role.value.description)
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST,role.value)
        }
    }

    @GetMapping
    fun getAll():ArrayList<RoleResponse>{
        return roleService.getRoles().map { elem -> RoleResponse(elem.id!!,elem.name,elem.description) } as ArrayList<RoleResponse>
    }

    @GetMapping("/secret-code/{id}")
    fun generateNewSecretCode(@PathVariable id: Long):SecretCode{
        when(val role=roleService.generateNewSecretCode(id)){
            is Either.Right ->{
                return role.value
            }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,role.value)

            }
        }
    }

    @PatchMapping("/secret-code")
    fun updateUserRoleViaSecretCode(@RequestBody payload:SecretCode,@RequestHeader("Authorization") token: String):RoleUpdateResponse{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        when(val updatedUser=roleService.updateUserRoleViaSecretCode(payload.code, user)){
            is Either.Right ->{
                return RoleUpdateResponse(updatedUser.value.id!!,updatedUser.value.username,updatedUser.value.firstname,updatedUser.value.lastname,updatedUser.value.roleId)
            }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,updatedUser.value)
            }
        }
    }

    @PatchMapping
    fun updateUserRole(@RequestBody payload: UserUpdateRoleRequest):LoginUser{
        when(val updatedUser=roleService.updateUserRole(payload.roleId,payload.userId)){
            is Either.Right ->{
                return updatedUser.value
            }
            is Either.Left ->{
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,updatedUser.value)
            }
        }
    }
}