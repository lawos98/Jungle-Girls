package pl.edu.agh.ii.io.jungleGirls.service

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
}