package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.repository.StudentRepository
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser


@Service
class StudentService(
    private val studentDao: StudentRepository
) {

    fun findByIndex(index:Long):LoginUser?{
        return studentDao.findById(index).block()
    }

    fun findByUsername(username:String):LoginUser?{
        return studentDao.findByUsername(username).block()
    }

    fun createUser(user: LoginUser): Either<String, LoginUser> {
        return validateNewUser(user).flatMap { _ ->
            try{ return studentDao.save(user).block()!!.right()}
            catch (e:DataIntegrityViolationException){
                return "User with this email already exists".left()
            }
            catch (e:Error){
                return "Unknown error".left()
            }
        }
    }
    fun login(username:String, password:String): Either<String,LoginUser>{
        return checkEmptyName(username).flatMap { _ -> checkEmptyPassword(password)}.flatMap { _ ->
            val user = studentDao.findByUsername(username).block()
                ?: return "No user found".left()
            return user.right()
        }
    }
    private fun checkEmptyName(username:String):Either<String, None> {
        if(username.isBlank()){
            return "Username cannot be empty".left()
        }
        return None.right()
    }

    private fun checkEmptyPassword(password:String):Either<String, None> {
        if(password.isBlank()){
            return "Password cannot be empty".left()
        }
        return None.right()
    }

    private fun checkGithubLink(link:String):Either<String, None> {
        if(!link.matches("/((git|ssh|http(s)?)|(git@[\\w.]+))(:(//)?)([\\w.@:/\\-~]+)(\\.git)(/)?/g".toRegex())){
            return "Incorrect GitHub link".left()
        }
        return None.right()
    }

    private fun validateNewUser(user:LoginUser):Either<String, None> {
        return checkEmptyName(user.username).flatMap { _ -> checkEmptyPassword(user.password) }
    }
}