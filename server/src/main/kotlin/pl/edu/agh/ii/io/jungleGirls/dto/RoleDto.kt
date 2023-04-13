package pl.edu.agh.ii.io.jungleGirls.dto

data class SecretCode(val code:String)
data class SecretCodeRequest(val roleId:Long)

data class UserUpdateRoleRequest(val roleId:Long,val userId:Long)