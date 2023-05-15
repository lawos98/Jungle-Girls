package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.RolePermissionRequest
import pl.edu.agh.ii.io.jungleGirls.model.RolePermission
import pl.edu.agh.ii.io.jungleGirls.service.RolePermissionService

@RestController
@RequestMapping("/api/role-permission")
class RolePermissionController(
    private val rolePermissionService: RolePermissionService
) {
    @PutMapping
    fun updateRolePermission(@RequestBody payload:RolePermissionRequest): RolePermission {
        when (val rolePermission=rolePermissionService.updateRolePermission(payload)){
            is Either.Right ->{
                return rolePermission.value
            }
            is Either.Left ->{
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,rolePermission.value)
            }
        }
    }
}
