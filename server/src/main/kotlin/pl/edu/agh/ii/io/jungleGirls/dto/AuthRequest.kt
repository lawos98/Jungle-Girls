package pl.edu.agh.ii.io.jungleGirls.dto

data class LoginDto(
    val name: String,
    val password: String,
)

data class RegisterDto(
    val index: Long,
    val name: String,
    val password: String
)