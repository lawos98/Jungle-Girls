package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.dto.SecretCodeResponse
import pl.edu.agh.ii.io.jungleGirls.repository.RoleRepository
import pl.edu.agh.ii.io.jungleGirls.util.Bcrypt

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {

    fun generateNewSecretCode(roleId:Long):Either<String,SecretCodeResponse>{
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + "!@#$%^&*?".toList()
        val newSecretCode = (1..20).map { allowedChars.random() }.joinToString("")
        val role = roleRepository.findById(roleId).block() ?: return "Role with that ID does not exist".left()
        role.secretCode=Bcrypt.hashBcrypt(newSecretCode)
        roleRepository.save(role).block()?.right() ?:  "No role permission found".left()
        return SecretCodeResponse(newSecretCode).right()
    }
}