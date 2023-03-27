package pl.edu.agh.ii.io.jungleGirls.dto

import org.springframework.web.server.ResponseStatusException

class ApiException(code: Int, message: String) : ResponseStatusException(code, message, null)

data class LoginResponseDto(
    val token: String,
)
