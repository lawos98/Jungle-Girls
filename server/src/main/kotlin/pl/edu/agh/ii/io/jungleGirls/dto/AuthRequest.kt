package pl.edu.agh.ii.io.jungleGirls.dto

data class LoginRequest(
    val username: String,
    val password: String,
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val firstname: String,
    val lastname: String
)