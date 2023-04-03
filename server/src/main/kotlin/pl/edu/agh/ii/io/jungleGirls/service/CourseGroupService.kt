package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.repository.CourseGroupRepository

@Service
class CourseGroupService(private val courseGroupRepository: CourseGroupRepository){
    fun existsByName(name: String):Boolean{
        return courseGroupRepository.existsByName(name).block() ?: false
    }

    private fun checkIsEmpty(names: ArrayList<String>): Either<String, None> {
        return if(names.isEmpty()) "Group name not specified".left() else None.right()
    }
    private fun checkIfCourseGroupsExist(names: ArrayList<String>): Either<String,ArrayList<Long>> {
        val ids = ArrayList<Long>()
        for(name in names){
            val id = courseGroupRepository.getIdByName(name).block() ?: return "group does not exists".left()
            ids.add(id)
        }
        return ids.right()
    }

    fun validateNames(names: ArrayList<String>): Either<String, ArrayList<Long>> {
        return checkIsEmpty(names).flatMap { _ -> checkIfCourseGroupsExist(names)}
    }

    fun getAllNames():ArrayList<String>{
        return courseGroupRepository.findAllNames().collectList().block() as ArrayList<String>
    }
}