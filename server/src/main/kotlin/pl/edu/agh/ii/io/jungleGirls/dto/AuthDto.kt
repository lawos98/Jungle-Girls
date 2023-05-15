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
data class AuthResponse(
    val userId: Long,
    val roleId: Long,
    val username: String,
    val firstname: String,
    val lastname: String,
    val token: String,
)

data class UpdateUserRequest(
    val username: String,
    val firstname: String,
    val lastname: String,
)
