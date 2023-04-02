package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.repository.ActivityCategoryRepository

@Service
class ActivityCategoryService(private val activityCategoryRepository: ActivityCategoryRepository){
    fun existsById(id: Long):Boolean{
        return activityCategoryRepository.existsById(id).block() ?: false
    }
    private fun checkIfNameIsBlank(name: String): Either<String, None> {
        return if(name.isBlank()) "Activity category name is empty!".left() else None.right()
    }
    private fun checkIfCategoryExists(name: String): Either<String, Long> {
         return when(val id = getIdByName(name)) {
            null -> { "Activity category does not exist!".left() }
            else -> { id.right() }
        }

    }

    fun validateName(name: String): Either<String, Long> {
        return checkIfNameIsBlank(name).flatMap { _ -> checkIfCategoryExists(name)}
    }

    fun getIdByName(name: String):Long?{
        return activityCategoryRepository.getIdByName(name).block()
    }
}