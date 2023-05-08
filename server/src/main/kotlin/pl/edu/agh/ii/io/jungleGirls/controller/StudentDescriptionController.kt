package pl.edu.agh.ii.io.jungleGirls.controller

import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.edu.agh.ii.io.jungleGirls.dto.StudentDescriptionRequest
import pl.edu.agh.ii.io.jungleGirls.model.StudentDescription
import pl.edu.agh.ii.io.jungleGirls.service.StudentDescriptionService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService

@RestController
@RequestMapping("api/student-description")
class StudentDescriptionController(
    private val tokenService: TokenService,
    private val studentDescriptionService: StudentDescriptionService,
) {

    @GetMapping
    fun getStudentDescription(@RequestHeader("Authorization") token: String): StudentDescription {
        val user = tokenService.parseToken(token.substring("Bearer".length))
        return when (val description = studentDescriptionService.findByUserId(user.id)) {
            is Either.Right -> description.value
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, description.value)
        }
    }
    @PatchMapping
    fun updateStudentDescription(@RequestHeader("Authorization") token: String,@RequestBody payload: StudentDescriptionRequest) :StudentDescription{
        val user = tokenService.parseToken(token.substring("Bearer".length))
        when (val description = studentDescriptionService.findByUserId(user.id)) {
            is Either.Right -> when (val updatedDescription = studentDescriptionService.updateStudentDescription(description.value.id,payload.index,payload.githubLink)){
                    is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST,updatedDescription.value)
                    is Either.Right -> return updatedDescription.value
                }
            is Either.Left -> throw ResponseStatusException(HttpStatus.BAD_REQUEST,description.value)
        }
    }

}
