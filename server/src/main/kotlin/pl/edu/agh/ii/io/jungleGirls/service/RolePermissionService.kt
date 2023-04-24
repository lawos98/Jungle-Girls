package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.dto.RolePermissionRequest
import pl.edu.agh.ii.io.jungleGirls.enum.Permissions
import pl.edu.agh.ii.io.jungleGirls.model.RolePermission
import pl.edu.agh.ii.io.jungleGirls.repository.RolePermissionRepository
import pl.edu.agh.ii.io.jungleGirls.repository.RoleRepository

@Service
class RolePermissionService(
    private val rolePermissionRepository: RolePermissionRepository,
    private val roleRepository: RoleRepository,
    private val permissionRepository: RolePermissionRepository
) {
    fun getPermissionNamesByRoleId(roleId: Long):ArrayList<RolePermission>{
        return rolePermissionRepository.getPermissionNamesByRoleId(roleId).collectList().block() as ArrayList<RolePermission>
    }

    fun updateRolePermission(role: RolePermissionRequest): Either<String, RolePermission> {
        return validateRolePermissionUpdate(role).flatMap { _ ->
            val updatedRolePermission = rolePermissionRepository.updateRolePermission(role.roleId,role.permissionId,role.shouldBeDisplayed).block()
            return updatedRolePermission?.right() ?: "No role permission found".left()
        }
    }

    private fun checkIfRoleExists(roleId:Long):Either<String,None>{
        return if(roleRepository.existsById(roleId).block()==true) None.right()
        else "Role with that ID does not exist".left()
    }

    private fun checkIfPermissionExists(permissionId:Long):Either<String,None>{
        return if(permissionRepository.existsById(permissionId).block()==true) None.right()
        else "Permission with that ID does not exist".left()
    }
    private fun validateRolePermissionUpdate(role:RolePermissionRequest):Either<String,None>{
        return checkIfPermissionExists(role.permissionId).flatMap { _ -> checkIfRoleExists(role.roleId) }
    }

    fun checkUserPermission(userId:Long,permissions: Permissions):Boolean{
        return rolePermissionRepository.checkUserPermission(userId,permissions.getName()).block() ?: return false
    }
}
