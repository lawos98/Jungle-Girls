package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.ii.io.jungleGirls.dto.ApiException
import pl.edu.agh.ii.io.jungleGirls.dto.LoginDto
import pl.edu.agh.ii.io.jungleGirls.dto.LoginResponseDto
import pl.edu.agh.ii.io.jungleGirls.dto.RegisterDto
import pl.edu.agh.ii.io.jungleGirls.model.Student
import pl.edu.agh.ii.io.jungleGirls.service.TokenService
import pl.edu.agh.ii.io.jungleGirls.service.StudentService

@RestController
@RequestMapping("/api")
class AuthController(
    private val tokenService: TokenService,
    private val studentService: StudentService,
) {
    @PostMapping("/login")
    fun login(@RequestBody payload: LoginDto): LoginResponseDto {
        when(val user = studentService.login(payload.name,payload.password)){
            is Either.Right -> {
                return LoginResponseDto(
                    token = tokenService.createToken(user.value),
                )
            }
            is Either.Left -> {
                throw ApiException(400,user.value)
            }
        }
    }

    @PostMapping("/register")
    fun register(@RequestBody payload: RegisterDto): LoginResponseDto {
        if (studentService.findByUsername(payload.name)!=null) {
            throw ApiException(400, "Name already exists")
        }

        val user = Student(
            index = payload.index,
            nick = payload.name,
            password = payload.password
        )

        when(val savedUser = studentService.createOrUpdateUser(user)){
            is Either.Right -> {
                return LoginResponseDto(
                    token = tokenService.createToken(savedUser.value),
                )
            }
            is Either.Left -> {
                throw ApiException(401,savedUser.value)
            }
        }
    }
}