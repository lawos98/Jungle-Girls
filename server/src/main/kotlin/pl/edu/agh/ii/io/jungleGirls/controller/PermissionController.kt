package pl.edu.agh.ii.io.jungleGirls.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.agh.ii.io.jungleGirls.model.Permission
import pl.edu.agh.ii.io.jungleGirls.service.PermissionService

@RestController
@RequestMapping("/api/permission")
class PermissionController(
    private val permissionService: PermissionService
) {
    @GetMapping
    fun getAllPermissions(): ArrayList<Permission>{
        return permissionService.getAll()
    }
}