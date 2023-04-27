package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.dto.ActivityIdWithName
import pl.edu.agh.ii.io.jungleGirls.dto.ScoreWithActivityIdAndStudentId
import pl.edu.agh.ii.io.jungleGirls.dto.StudentIdWithIndex
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroup
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.repository.CourseGroupRepository

@Service
class CourseGroupService(private val courseGroupRepository: CourseGroupRepository){
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

    fun getAllStudentIdsAndIndexesByGroupId(groupId: Long):HashMap<Long,String?>{
        return courseGroupRepository.getAllStudentIdsAndIndexesByGroupId(groupId).collectMap(StudentIdWithIndex::id, StudentIdWithIndex::index).block() as HashMap<Long, String?>
    }

    fun getAllActivityIdsAndNames(groupId:Long):HashMap<Long,String>{
        return courseGroupRepository.getAllActivityIdsAndNamesByGroupId(groupId).collectMap(ActivityIdWithName::id, ActivityIdWithName::name).block() as HashMap<Long, String>
    }

    fun getAllScoresWithActivityIdAndStudentIdByGroupId(groupId:Long):HashMap<Pair<Long,Long>,Double>{
        return courseGroupRepository.getAllScoresWithActivityIdAndStudentIdByGroupId(groupId).collectMap(ScoreWithActivityIdAndStudentId::getKey,ScoreWithActivityIdAndStudentId::score).block() as HashMap<Pair<Long, Long>, Double>
    }

}
