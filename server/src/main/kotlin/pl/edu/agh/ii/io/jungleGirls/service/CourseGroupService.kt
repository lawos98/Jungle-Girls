package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.repository.CourseGroupRepository

@Service
class CourseGroupService(private val courseGroupRepository: CourseGroupRepository) {
    fun existsByName(name: String): Boolean {
        return courseGroupRepository.existsByName(name).block() ?: false
    }

    fun validateNames(instructorId: Long, names: ArrayList<String>): Either<String, ArrayList<Long>> {
        if (names.isEmpty()) return "Group names not specified".left()
        val result = getAllNamesById(instructorId)
        if (!result.containsAll(names) || !names.containsAll(result)) return "wrong group names".left()
        return (names.map { courseGroupRepository.getIdByName(it).block() ?: return "group does not exists".left() } as ArrayList).right()
    }

    fun getAllNamesById(instructorId: Long): ArrayList<String> {
        return courseGroupRepository.findAllNamesById(instructorId).collectList().block() as ArrayList<String>
    }
}
