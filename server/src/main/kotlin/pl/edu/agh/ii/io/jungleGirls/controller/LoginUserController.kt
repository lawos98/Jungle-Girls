package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.UpdateUserRequest
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.service.LoginUserService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService

@RestController
@RequestMapping("/api/user")
class LoginUserController(
    private val loginUserService: LoginUserService,
    private val tokenService: TokenService,
) {
    @PatchMapping
    fun updateUser(@RequestBody payload: UpdateUserRequest,@RequestHeader("Authorization") token: String): LoginUser {
        val user = tokenService.parseToken(token.substring("Bearer".length))
        return when(val updatedUser = loginUserService.updateUser(payload,user)){
            is Either.Right -> updatedUser.value
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, updatedUser.value)
        }
    }
}
