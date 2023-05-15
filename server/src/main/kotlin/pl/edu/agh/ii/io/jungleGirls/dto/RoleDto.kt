package pl.edu.agh.ii.io.jungleGirls.dto
data class UserUpdateRoleRequest(val roleId:Long,val userId:Long)
data class RoleResponse(val roleId: Long,val name: String,val description:String)
