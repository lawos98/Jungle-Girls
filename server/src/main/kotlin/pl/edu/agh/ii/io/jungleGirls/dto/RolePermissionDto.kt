package pl.edu.agh.ii.io.jungleGirls.dto

data class RolePermissionRequest(
    val roleId: Long,
    val permissionId:Long,
    val shouldBeDisplayed: Boolean
)
