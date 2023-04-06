package pl.edu.agh.ii.io.jungleGirls.service

import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.model.Permission
import pl.edu.agh.ii.io.jungleGirls.repository.PermissionRepository

@Service
class PermissionService(
    private val permissionRepository: PermissionRepository
) {
    fun findById(id:Long): Permission{
        return permissionRepository.findById(id).block() ?: throw IllegalStateException("User id not found")
    }
}