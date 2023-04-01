package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.repository.CourseGroupRepository

@Service
class CourseGroupService(private val courseGroupRepository: CourseGroupRepository){
    fun existsByName(name: String):Boolean{
        return courseGroupRepository.existsByName(name).block() ?: false
    }

//    private fun checkIfAllCourseGroupsExist(names:ArrayList<String>) : Either<String, None>{
//        return if(names.all{existsByName(it)}) None.right() else "Requested group does not exist".left()
//    }
//
//    private fun checkIsEmpty(names:ArrayList<String>): Either<String, None>{
//        return if(names.isNotEmpty()) None.right() else "Did not specified course group".left()
//    }

    fun getIds(names:ArrayList<String>): ArrayList<Long>? {
        if(names.isNotEmpty()) return null
        val ids = ArrayList<Long>()
        for(name in names){
            val id = courseGroupRepository.getIdByName(name).block() ?: return null
            ids.add(id)
        }
        return ids
    }
}