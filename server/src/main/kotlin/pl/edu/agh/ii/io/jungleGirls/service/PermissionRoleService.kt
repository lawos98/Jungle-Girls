package pl.edu.agh.ii.io.jungleGirls.service

import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.model.PermissionRole
import pl.edu.agh.ii.io.jungleGirls.repository.PermissionRoleRepository

@Service
class PermissionRoleService(
    private val permissionRoleRepository: PermissionRoleRepository
) {
    fun getPermissionNamesByRoleId(roleId: Long):ArrayList<PermissionRole>{
        return permissionRoleRepository.getPermissionNamesByRoleId(roleId).collectList().block() as ArrayList<PermissionRole>
    }
}