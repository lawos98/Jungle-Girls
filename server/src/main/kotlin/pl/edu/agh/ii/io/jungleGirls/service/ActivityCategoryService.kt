package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.model.ActivityCategory
import pl.edu.agh.ii.io.jungleGirls.repository.ActivityCategoryRepository
import pl.edu.agh.ii.io.jungleGirls.util.checkIsBlank

@Service
class ActivityCategoryService(private val activityCategoryRepository: ActivityCategoryRepository){
    fun existsById(id: Long):Boolean{
        return activityCategoryRepository.existsById(id).block() ?: false
    }
    private fun checkIfCategoryExistsById(instructorId: Long, name: String): Either<String, Long> {
         return when(val id = getIdByInstructorIdAndName(instructorId,name)) {
            null -> { "Activity category does not exist!".left() }
            else -> { id.right() }
        }

    }

    fun validateName(instructorId: Long, name: String): Either<String, Long> {
        return checkIsBlank(name,"Activity category name is empty!").flatMap { _ -> checkIfCategoryExistsById(instructorId,name)}
    }

    fun getIdByInstructorIdAndName(instructorId: Long, name: String):Long?{
        return activityCategoryRepository.getIdByInstructorIdAndName(instructorId,name).block()
    }

    fun getAllNames(instructorId:Long):ArrayList<String>{
        return activityCategoryRepository.findAllNamesById(instructorId).collectList().block() as ArrayList<String>
    }

    fun existsByName(name: String): Boolean {
        return activityCategoryRepository.existsByName(name).block() ?: false
    }

    private fun checkIfNameIsNotTaken(name: String):Either<String, None>{
        return if(existsByName(name)) "Activity category name is already taken!".left() else None.right()
    }

    private fun validateActivityCategory(activityCategory: ActivityCategory): Either<String, None> {
        return checkIsBlank(activityCategory.name,"name can not be empty")
            .flatMap {_ -> checkIfNameIsNotTaken(activityCategory.name)
                .flatMap {_ -> checkIsBlank(activityCategory.description,"description can not be empty") }}
    }

    public fun createCategory(activityCategory: ActivityCategory): Either<String, None>{
        return validateActivityCategory(activityCategory).flatMap { _ ->
            activityCategoryRepository.save(activityCategory).block() ?: return "Error while saving activity category".left()
            return None.right()
        }
    }
}