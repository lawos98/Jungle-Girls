package pl.edu.agh.ii.io.jungleGirls.dto

data class SecretCode(val code:String)

data class CreateCourseGroup(val name:String)

data class CourseGroupRespone(val id:Long,val name:String,val instructorId:Long)
