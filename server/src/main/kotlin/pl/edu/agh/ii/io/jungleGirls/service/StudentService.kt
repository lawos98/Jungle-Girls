package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.db.StudentDao
import pl.edu.agh.ii.io.jungleGirls.model.Student


@Service
class StudentService(
    private val studentDao: StudentDao
) {

    fun findByIndex(index:Long):Student?{
        return studentDao.findByIndex(index).block()
    }

    fun findByUsername(username:String):Student?{
        return studentDao.findByNick(username).block()
    }

    fun createOrUpdateUser(user: Student): Either<String, Student> {
        return validateUser(user).flatMap { _ ->
            try{ return studentDao.save(user).block()!!.right()}
            catch (e:DataIntegrityViolationException){
                return "User with this email already exists".left()
            }
            catch (e:Error){
                return "Unknown error".left()
            }
        }
    }
    fun login(username:String, password:String): Either<String,Student>{
        return checkEmptyName(username).flatMap { _ -> checkEmptyPassword(password)}.flatMap { _ ->
            val user = studentDao.findByNick(username).block()
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
        if(!link.matches("/((git|ssh|http(s)?)|(git@[\\w\\.]+))(:(//)?)([\\w\\.@\\:/\\-~]+)(\\.git)(/)?/g".toRegex())){
            return "Incorrect GitHub link".left()
        }
        return None.right()
    }

    private fun validateUser(user:Student):Either<String, None> {
        return checkEmptyName(user.nick).flatMap { _ -> checkEmptyPassword(user.password) }.flatMap{_ -> checkGithubLink(user.github_link)}
    }
}