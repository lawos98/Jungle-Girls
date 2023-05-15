package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.*
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.service.RoleService

@RestController
@RequestMapping("/api/role")
class RoleController(
    private val roleService: RoleService,
) {
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long):RoleResponse{
        when(val role = roleService.getRoleById(id)){
            is Either.Right -> return RoleResponse(role.value.id,role.value.name,role.value.description)
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST,role.value)
        }
    }

    @GetMapping
    fun getAll():ArrayList<RoleResponse>{
        return roleService.getRoles().map { elem -> RoleResponse(elem.id,elem.name,elem.description) } as ArrayList<RoleResponse>
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
