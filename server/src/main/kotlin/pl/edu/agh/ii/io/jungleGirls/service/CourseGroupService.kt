package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityIdWithName
import pl.edu.agh.ii.io.jungleGirls.dto.ScoreWithActivityIdAndStudentId
import pl.edu.agh.ii.io.jungleGirls.dto.SecretCode
import pl.edu.agh.ii.io.jungleGirls.dto.StudentIdWithName
import pl.edu.agh.ii.io.jungleGirls.enum.Roles
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroup
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.model.StudentDescription
import pl.edu.agh.ii.io.jungleGirls.repository.CourseGroupRepository
import pl.edu.agh.ii.io.jungleGirls.util.Bcrypt

@Service
class CourseGroupService(
    private val courseGroupRepository: CourseGroupRepository,
    private val studentDescriptionService: StudentDescriptionService,
    private val roleService: RoleService
) {
    fun existsByName(name: String):Boolean{
        return courseGroupRepository.existsByName(name).block() ?: false
    }

    fun getNameById(groupId: Long):String?{
        return courseGroupRepository.findNameById(groupId).block()
    }

    fun validateNames(instructorId : Long, names: ArrayList<String>): Either<String, ArrayList<Long>> {
        if (names.isEmpty()) return "Group names not specified".left()
        val result = getAllNamesById(instructorId)
        if (!result.containsAll(names) || !names.containsAll(result)) return "wrong group names".left()
        return (names.map{ courseGroupRepository.getIdByName(it).block() ?: return "group does not exists".left()} as ArrayList).right()
    }

    fun getAllNamesById(instructorId: Long):ArrayList<String>{
        return courseGroupRepository.findAllNamesById(instructorId).collectList().block() as ArrayList<String>
    }

    fun getAllStudentsByGroupId(groupId: Long):ArrayList<LoginUser>{
        return courseGroupRepository.getAllStudentsByGroupId(groupId).collectList().block() as ArrayList<LoginUser>
    }

    fun checkLecturerGroup(lecturerId:Long, groupId:Long):Boolean{
        return courseGroupRepository.checkLecturerGroup(lecturerId,groupId).collectList().block()?.isNotEmpty() ?: return false
    }

    fun existsByCourseId(courseId: Long):Boolean{
        return courseGroupRepository.existsById(courseId).block() ?: return false
    }

    fun getAllGroups(instructorId:Long):ArrayList<CourseGroup>{
        return courseGroupRepository.getAllGroups(instructorId).collectList().block() as ArrayList<CourseGroup>
    }

    fun getAllStudentIdsAndNamesByGroupId(groupId: Long):HashMap<Long,String>{
        return courseGroupRepository.getAllStudentIdsAndNamesByGroupId(groupId).collectMap(StudentIdWithName::id, StudentIdWithName::name).block() as HashMap<Long, String>
    }

    fun getAllStudentIdsAndUserNamesByGroupId(groupId: Long):HashMap<Long,String>{
        return courseGroupRepository.getAllStudentIdsAndUserNamesByGroupId(groupId).collectMap(StudentIdWithName::id, StudentIdWithName::name).block() as HashMap<Long, String>
    }

    fun getAllActivityIdsAndNames(groupId:Long):HashMap<Long,String>{
        return courseGroupRepository.getAllActivityIdsAndNamesByGroupId(groupId).collectMap(ActivityIdWithName::id, ActivityIdWithName::name).block() as HashMap<Long, String>
    }

    fun getAllScoresWithActivityIdAndStudentIdByGroupId(groupId:Long):HashMap<Pair<Long,Long>,Double>{
        return courseGroupRepository.getAllScoresWithActivityIdAndStudentIdByGroupId(groupId).collectMap(ScoreWithActivityIdAndStudentId::getKey,ScoreWithActivityIdAndStudentId::score).block() as HashMap<Pair<Long, Long>, Double>
    }

    fun generateNewSecretCode(groupId:Long):Either<String, SecretCode>{
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        var newSecretCode = (1..8).map { allowedChars.random() }.joinToString("")
        while(courseGroupRepository.findAll().collectList().block()?.any{ elem -> elem.secretCode?.let { Bcrypt.checkBcrypt(newSecretCode, it) } == true } == true){
            newSecretCode = (1..8).map { allowedChars.random() }.joinToString("")
        }
        val group = courseGroupRepository.findById(groupId).block() ?: return "Group with that ID does not exist".left()
        group.secretCode=Bcrypt.hashBcrypt(newSecretCode)
        courseGroupRepository.save(group).block()?.right() ?:  "No group found found".left()
        return SecretCode(newSecretCode).right()
    }

    private fun findGroupBySecretCode(secretCode: String):Either<String,CourseGroup>{
        val groupList= courseGroupRepository.findAll().collectList().block() ?: return "No role exists".left()
        val group = groupList.filter { elem -> elem.secretCode?.let { Bcrypt.checkBcrypt(secretCode, it) } == true  }
        if (group.isEmpty()) return "Role with that secret code does not exist".left()
        if (group.size > 1) return "Multiple roles with that secret code".left()
        return group[0].right()
    }

    fun updateGroupViaSecretCode(secretCode: String, user: LoginUser): Either<String, StudentDescription> {
        return findGroupBySecretCode(secretCode)
            .flatMap { group ->
                roleService.updateUserRole(Roles.STUDENT.getId(), user.id)
                    .flatMap {
                        studentDescriptionService.addStudentDescription(user.id, group.id)
                    }
            }
    }

    fun createCourseGroup(name:String, instructorId: Long): Either<String, CourseGroup> {
        return courseGroupRepository.createCourseGroup(name, instructorId).block()?.right() ?: "Server error group cannot be created".left()
    }

}
