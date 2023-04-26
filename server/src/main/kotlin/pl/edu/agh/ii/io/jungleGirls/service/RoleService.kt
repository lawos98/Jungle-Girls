package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.dto.SecretCode
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.model.Role
import pl.edu.agh.ii.io.jungleGirls.repository.LoginUserRepository
import pl.edu.agh.ii.io.jungleGirls.repository.RoleRepository
import pl.edu.agh.ii.io.jungleGirls.util.Bcrypt

@Service
class RoleService(
    private val roleRepository: RoleRepository,
    private val loginUserRepository: LoginUserRepository,
) {

    fun generateNewSecretCode(roleId:Long):Either<String,SecretCode>{
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        var newSecretCode = (1..8).map { allowedChars.random() }.joinToString("")
        while(roleRepository.findAll().collectList().block()?.any{ elem -> Bcrypt.checkBcrypt(newSecretCode, elem.secretCode) } ?: return "No role exists".left()){
            newSecretCode = (1..8).map { allowedChars.random() }.joinToString("")
        }
        val role = roleRepository.findById(roleId).block() ?: return "Role with that ID does not exist".left()
        role.secretCode=Bcrypt.hashBcrypt(newSecretCode)
        roleRepository.save(role).block()?.right() ?:  "No role permission found".left()
        return SecretCode(newSecretCode).right()
    }

    private fun findRoleBySecretCode(secretCode: String):Either<String,Role>{
        val roleList=roleRepository.findAll().collectList().block() ?: return "No role exists".left()
        val role = roleList.filter { elem -> Bcrypt.checkBcrypt(secretCode,elem.secretCode)}
        if (role.isEmpty()) return "Role with that secret code does not exist".left()
        if (role.size > 1) return "Multiple roles with that secret code".left()
        return role[0].right()
    }

    fun updateUserRoleViaSecretCode(secretCode:String,user:LoginUser):Either<String,LoginUser>{
        when(val role = findRoleBySecretCode(secretCode)){
            is Either.Left -> return role.value.left()
            is Either.Right -> {
                val updateUser = loginUserRepository.updateUserRole(role.value.id, user.id).block() ?: return "Server error cannot update user role".left()
                return updateUser.right()
            }
        }
    }

    fun updateUserRole(roleId:Long,userId:Long):Either<String,LoginUser>{
        loginUserRepository.findById(userId).block() ?: return "No user with that id exists".left()
        roleRepository.findById(roleId).block() ?: return "No role with that id exists".left()
        return loginUserRepository.updateUserRole(roleId,userId).block()?.right() ?: return "Server error cannot update user role".left()
    }

    fun getRoleById(roleId:Long):Either<String,Role>{
        val role=roleRepository.findById(roleId).block() ?: return "No role with that id exists".left()
        return role.right()
    }

    fun getRoles():ArrayList<Role>{
        return roleRepository.findAll().collectList().block() as ArrayList<Role>
    }
    fun getRoleByUserId(userId:Long):Either<String,Role>{
        return roleRepository.findByUserId(userId).block()?.right() ?: return "User with that id does not exist".left()
    }

}
