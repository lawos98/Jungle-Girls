package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.repository.ActivityTypeRepository
import pl.edu.agh.ii.io.jungleGirls.util.checkIsBlank

@Service
class ActivityTypeService(private val activityTypeRepository: ActivityTypeRepository) {
    fun existsById(id: Long): Boolean {
        return activityTypeRepository.existsById(id).block() ?: false
    }

    fun getIdByName(name: String): Long? {
        return activityTypeRepository.getIdByName(name).block()
    }

    private fun checkIfTypeExists(name: String): Either<String, Long> {
        return when (val id = getIdByName(name)) {
            null -> { "Activity type does not exist!".left() }
            else -> { id.right() }
        }
    }

    fun validateName(name: String): Either<String, Long> {
        return checkIsBlank(name, "Activity type name is empty!").flatMap { _ ->
            checkIfTypeExists(
                name
            )
        }
    }

    fun getAllNames(): ArrayList<String> {
        return activityTypeRepository.findAllNames().collectList().block() as ArrayList<String>
    }

    fun getNameById(activityTypeId: Long): String? {
        return activityTypeRepository.getNameById(activityTypeId).block()
    }
}
