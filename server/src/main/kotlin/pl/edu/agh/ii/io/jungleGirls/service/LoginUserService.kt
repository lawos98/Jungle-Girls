package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.repository.LoginUserRepository
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.util.Bcrypt


@Service
class LoginUserService(
    private val loginUserRepository: LoginUserRepository
) {

    fun findByIndex(index: Long): LoginUser? {
        return loginUserRepository.findById(index).block()
    }

    fun findByUsername(username: String): LoginUser? {
        return loginUserRepository.findByUsername(username).block()
    }

    fun createUser(user: LoginUser): Either<String, LoginUser> {
        return validateNewUser(user).flatMap { _ ->
            user.password=Bcrypt.hashBcrypt(user.password)
            val loginUser = loginUserRepository.save(user).block() ?: return "Error while saving User".left()
            return loginUser.right()
        }
    }

    fun login(username: String, password: String): Either<String, LoginUser> {
        return checkBlankUser(username, password).flatMap { _ ->
            val user = loginUserRepository.findByUsername(username).block() ?: return "No user found".left()
            println(Bcrypt.hashBcrypt(password))
            println(user.password)
            println(Bcrypt.checkBcrypt(password, user.password))
            if (Bcrypt.checkBcrypt(password, user.password)) return user.right()
            return "Not correct password".left()
        }
    }

    private fun checkIsBlank(username: String, errorMessage: String): Either<String, None> {
        if (username.isBlank()) {
            return errorMessage.left()
        }
        return None.right()
    }

    private fun checkPassword(password: String): Either<String, None> {
//        At least one upper case English letter
//        At least one lower case English letter
//        At least one digit
//        At least one special character
//        Minimum 8 in length
        if (!password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$".toRegex())) {
            return "Password must meet the requirements".left()
        }
        return None.right()
    }

    private fun checkUsername(username: String): Either<String,None>{
//        Alphanumeric string that may include _ and - having a length of 3 to 20 characters.
        if(!username.matches("^[a-z0-9_-]{3,20}\$".toRegex())){
            return "Username must meet the requirements".left()
        }
        return None.right()
    }

    private fun checkName(name: String,errorMessage: String): Either<String,None>{
        if(!name.matches("[A-Z][a-z]*".toRegex())){
            return errorMessage.left()
        }
        return None.right()
    }

    private fun checkBlankUser(username: String, password: String): Either<String, None> {
        return checkIsBlank(username, "Username cannot be empty")
            .flatMap { _ -> checkIsBlank(password,"Password cannot be empty") }
    }

    private fun validateNewUser(user: LoginUser): Either<String, None> {
        return checkBlankUser(user.username, user.password)
            .flatMap { _ -> checkIsBlank(user.firstname, "Firstname cannot be empty")
            .flatMap { _ -> checkIsBlank(user.lastname, "Lastname cannot be empty")
            .flatMap { _ -> checkName(user.firstname, "Firstname is incorrect")
            .flatMap { _ -> checkName(user.lastname, "Lastname is incorrect")
            .flatMap { _ -> checkUsername(user.username)
            .flatMap { _ -> checkPassword(user.password)}}}}}}
    }
}