package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.model.Role
import pl.edu.agh.ii.io.jungleGirls.repository.LoginUserRepository
import pl.edu.agh.ii.io.jungleGirls.repository.RoleRepository

@Service
class RoleService(
    private val roleRepository: RoleRepository,
    private val loginUserRepository: LoginUserRepository,
) {
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
