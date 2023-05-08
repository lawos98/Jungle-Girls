package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.LoginRequest
import pl.edu.agh.ii.io.jungleGirls.dto.AuthResponse
import pl.edu.agh.ii.io.jungleGirls.dto.RegisterRequest
import pl.edu.agh.ii.io.jungleGirls.service.TokenService
import pl.edu.agh.ii.io.jungleGirls.service.LoginUserService

@RestController
@RequestMapping("/api")
class AuthController(
    private val tokenService: TokenService,
    private val loginUserService: LoginUserService,
) {
    @PostMapping("/login")
    fun login(@RequestBody payload: LoginRequest): AuthResponse {
        when(val user = loginUserService.login(payload.username,payload.password)){
            is Either.Right -> {
                val loginUser=user.value
                return AuthResponse(
                    loginUser.id,
                    loginUser.roleId,
                    loginUser.username,
                    loginUser.firstname,
                    loginUser.lastname,
                    tokenService.createToken(loginUser),
                )
            }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, user.value)
            }
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody payload: RegisterRequest): AuthResponse {
        if (loginUserService.findByUsername(payload.username)!=null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists")
        }
        when(val savedUser = loginUserService.createUser(payload)){
            is Either.Right -> {
                val loginUser=savedUser.value
                return AuthResponse(
                    loginUser.id,
                    loginUser.roleId,
                    loginUser.username,
                    loginUser.firstname,
                    loginUser.lastname,
                    tokenService.createToken(loginUser),
                )
            }
            is Either.Left -> {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, savedUser.value)
            }
        }
    }
}
