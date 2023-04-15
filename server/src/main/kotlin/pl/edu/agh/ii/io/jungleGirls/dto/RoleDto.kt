package pl.edu.agh.ii.io.jungleGirls.dto

data class SecretCode(val code:String)
data class UserUpdateRoleRequest(val roleId:Long,val userId:Long)
data class RoleResponse(val roleId: Long,val name: String,val description:String)
data class RoleUpdateResponse(val id:Long,val username:String,val firstname:String,val lastname:String,val roleId:Long)