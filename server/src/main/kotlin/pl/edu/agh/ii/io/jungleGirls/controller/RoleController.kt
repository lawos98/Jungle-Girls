package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.SecretCodeRequest
import pl.edu.agh.ii.io.jungleGirls.dto.SecretCodeResponse
import pl.edu.agh.ii.io.jungleGirls.service.RoleService

@RestController
@RequestMapping("/api/role")
class RoleController(
    private val roleService: RoleService
) {
    @GetMapping("/secret-code")
    fun generateNewSecretCode(@RequestBody payload:SecretCodeRequest):SecretCodeResponse{
        when(val role=roleService.generateNewSecretCode(payload.roleId)){
            is Either.Right ->{
                return role.value
            }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,role.value)

            }
        }
    }

}