package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.repository.LoginUserRepository
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.util.Bcrypt
import pl.edu.agh.ii.io.jungleGirls.util.checkIsBlank


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
            if (Bcrypt.checkBcrypt(password, user.password)) return user.right()
            return "Not correct password".left()
        }
    }



    private fun checkPassword(password: String): Either<String, None> {
        if (!password.matches(Regex("^(?=.*[A-Z]).+$")))return "Password must have at least one upper case English letter".left()
        if (!password.matches(Regex("^(?=.*[a-z]).+$")))return "Password must have at least one lower case English letter".left()
        if (!password.matches(Regex("^(?=.*[0-9]).+$")))return "Password must have at least one digit".left()
        if (!password.matches(Regex("^(?=.*[#?!@\$%^&*-]).+$"))) return "Password must have at least one special character".left()
        if (!password.matches(Regex("^\\S+$"))) return "Password cannot have any white space characters".left()
        if (password.length<8) return "Password minimum length is 8".left()
        if (password.length>20) return "Password maximum length is 20".left()
        return None.right()
    }

    private fun checkUsername(username: String): Either<String,None>{
        if(username.matches("^[a-zA-Z0-9]$".toRegex()))return "Username must have only lowercase, uppercase letters and numbers".left()
        if (username.length<3) return "Username minimum length is 3".left()
        if (username.length>20) return "Username maximum length is 20".left()
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
            .flatMap { _ -> checkPassword(user.password) }
                        }}}}}
    }
}