package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.enum.Roles
import pl.edu.agh.ii.io.jungleGirls.model.StudentDescription
import pl.edu.agh.ii.io.jungleGirls.repository.StudentDescriptionRepository

@Service
class StudentDescriptionService(
    private val studentDescriptionRepository: StudentDescriptionRepository,
    private val roleService: RoleService
) {
    fun findByUserId(id: Long): Either<String,StudentDescription> {
        return when(val role = roleService.getRoleByUserId(id)){
            is Either.Left -> role.value.left()
            is Either.Right -> {
                if(role.value.id == Roles.STUDENT.getId()) studentDescriptionRepository.findById(id).block()?.right() ?: "Server cant find student description for id $id".left()
                else "That user is not a student".left()
            }
        }
    }
    fun getStudentGroupId(studentDescription: StudentDescription): Either<String,Long>{
        return studentDescription.courseGroupId?.right() ?: "Student does not belong to any group".left()
    }
    fun getUserGroupId(userId: Long): Either<String,Long>{
        return findByUserId(userId).flatMap { studentDescription -> getStudentGroupId(studentDescription) }
    }
    fun updateStudentDescription(userId:Long,index:Long?,githubLink:String?) :Either<String,StudentDescription>{
        return studentDescriptionRepository.updateDescription(userId,index,githubLink).block()?.right() ?: "Server cant update student description".left()
    }

    fun findById(id: Long): StudentDescription? {
        return studentDescriptionRepository.findById(id).block()
    }

    fun getMapOfStudentDescription(): MutableMap<Long, StudentDescription> {
        return studentDescriptionRepository.findAll().collectMap { it.id }.block() ?: mutableMapOf()
    }

    fun addStudentDescription(studentId:Long,courseGroupId:Long):Either<String,StudentDescription>{
        return studentDescriptionRepository.addStudentToGroup(studentId,courseGroupId).block()?.right() ?: "Server cant add student description".left()
    }
}
