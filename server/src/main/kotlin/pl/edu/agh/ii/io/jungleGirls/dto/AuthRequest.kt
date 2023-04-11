package pl.edu.agh.ii.io.jungleGirls.dto

data class LoginDto(
    val username: String,
    val password: String
)

data class RegisterDto(
    val username: String,
    val password: String,
    val firstname: String,
    val lastname: String
)
