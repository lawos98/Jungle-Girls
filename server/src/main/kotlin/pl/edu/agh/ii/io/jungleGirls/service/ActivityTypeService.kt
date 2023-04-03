package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.repository.ActivityCategoryRepository
import pl.edu.agh.ii.io.jungleGirls.repository.ActivityTypeRepository

@Service
class ActivityTypeService(private val activityTypeRepository: ActivityTypeRepository){
    fun existsById(id: Long):Boolean{
        return activityTypeRepository.existsById(id).block() ?: false
    }

    fun getIdByName(name: String):Long?{
        return activityTypeRepository.getIdByName(name).block()
    }

    private fun checkIfNameIsBlank(name: String): Either<String, None> {
        return if(name.isBlank()) "Activity type name is empty!".left() else None.right()
    }
    private fun checkIfTypeExists(name: String): Either<String, Long> {
        return when(val id = getIdByName(name)) {
            null -> { "Activity type does not exist!".left() }
            else -> { id.right() }
        }

    }

    fun validateName(name: String): Either<String, Long> {
        return checkIfNameIsBlank(name).flatMap { _ -> checkIfTypeExists(name)}
    }

    fun getAllNames():ArrayList<String>{
        val result = ArrayList<String>()
        activityTypeRepository.findAllNames().all{result.add(it)  }.block();
        return result;
    }
}