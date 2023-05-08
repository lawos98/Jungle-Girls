package pl.edu.agh.ii.io.jungleGirls.service

import arrow.core.*
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.model.ActivityCategory
import pl.edu.agh.ii.io.jungleGirls.repository.ActivityCategoryRepository
import pl.edu.agh.ii.io.jungleGirls.util.checkIsBlank

@Service
class ActivityCategoryService(private val activityCategoryRepository: ActivityCategoryRepository) {
    fun existsById(id: Long): Boolean {
        return activityCategoryRepository.existsById(id).block() ?: false
    }
    private fun checkIfCategoryExistsById(instructorId: Long, name: String): Either<String, Long> {
        return when (val id = getIdByInstructorIdAndName(instructorId, name)) {
            null -> { "Activity category does not exist!".left() }
            else -> { id.right() }
        }
    }
    fun deleteCategory(activityCategory: ActivityCategory): Either<String, None> {
        return checkIfCanBeDeleted(activityCategory.id).flatMap { _ ->
            activityCategoryRepository.delete(activityCategory).block()
            if (existsById(activityCategory.id)) return "Error while deleting activity category".left()
            return None.right()
        }
    }
    private fun canBeDeleted(id: Long): Boolean {
        return activityCategoryRepository.canBeDeleted(id).block() ?: false
    }

    private fun checkIfCanBeDeleted(id: Long): Either<String, Long> {
        return if (canBeDeleted(id)) id.right() else "There are activities belonging to this category!".left()
    }

    fun validateName(instructorId: Long, name: String): Either<String, Long> {
        return checkIsBlank(name, "Activity category name is empty!").flatMap { _ ->
            checkIfCategoryExistsById(
                instructorId,
                name
            )
        }
    }

    fun getIdByInstructorIdAndName(instructorId: Long, name: String): Long? {
        return activityCategoryRepository.getIdByInstructorIdAndName(instructorId, name).block()
    }

    fun getAllNames(instructorId: Long): ArrayList<String> {
        return activityCategoryRepository.findAllNamesById(instructorId).collectList().block() as ArrayList<String>
    }

    fun getNameByIdAndInstructorId(id: Long,instructorId: Long):String?{
        return activityCategoryRepository.getNameByIdAndInstructorId(id,instructorId).block()

    }

    fun existsByInstructorIdAndName(instructorId: Long, name: String): Boolean {
        return activityCategoryRepository.existsByInstructorIdAndName(instructorId, name).block() ?: false
    }

    private fun checkIfNameIsNotTaken(instructorId: Long, name: String): Either<String, None> {
        return if (existsByInstructorIdAndName(instructorId, name)) "Activity category name is already taken!".left() else None.right()
    }

    private fun validateActivityCategory(name:String,description: String,instructorId: Long): Either<String, None> {
        return checkIsBlank(name, "name can not be empty")
            .flatMap { _ ->
                checkIfNameIsNotTaken(
                    instructorId,
                    name
                )
                    .flatMap { _ ->
                        checkIsBlank(
                            description,
                            "description can not be empty"
                        )
                    }
            }
    }
    private fun checkIfEditedNameIsNotTaken(instructorId: Long,oldName: String, newName: String): Either<String, None> {
        return if (oldName != newName &&  existsByInstructorIdAndName(instructorId, newName)) "Activity category name is already taken!".left() else None.right()
    }
    private fun validateEditedActivityCategory(categoryToEdit: ActivityCategory, newName: String,newDescription: String,newInstructorId:Long): Either<String, None> {
        return checkIsBlank(newName, "name can not be empty")
            .flatMap { _ ->
                checkIfEditedNameIsNotTaken(
                    newInstructorId,
                    categoryToEdit.name,
                    newName,
                )
                    .flatMap { _ ->
                        checkIsBlank(
                            newDescription,
                            "description can not be empty"
                        )
                    }
            }
    }

    fun createCategory(name:String , description:String , instructorId: Long): Either<String, None> {
        return validateActivityCategory(name,description,instructorId).flatMap { _ ->
            activityCategoryRepository.save(name,description,instructorId).block() ?: return "Error while saving activity category".left()
            return None.right()
        }
    }

    fun findByInstructorIdAndName(instructorId: Long, name: String): ActivityCategory? {
        return activityCategoryRepository.findByInstructorIdAndName(instructorId, name).block()
    }

    fun getAllActivityCategoriesByInstructorId(instructorId: Long): List<ActivityCategory> {
        return activityCategoryRepository.getAllActivityCategoriesByInstructorId(instructorId).collectList().block() as ArrayList<ActivityCategory>
    }

    fun findByIdAndInstructorId(activityCategoryId: Long, instructorId: Long): ActivityCategory? {
        return activityCategoryRepository.findByIdAndInstructorId(activityCategoryId, instructorId).block()
    }

    fun editCategory(categoryToEdit: ActivityCategory, newName: String, newDescription:String,newInstructorId: Long): Either<String, None> {
        return validateEditedActivityCategory(categoryToEdit,newName,newDescription,newInstructorId).flatMap { _ ->
            activityCategoryRepository.update(categoryToEdit.id,newName,newDescription).block() ?: return "Error while editing activity category".left()
            return None.right()
        }
    }


}
