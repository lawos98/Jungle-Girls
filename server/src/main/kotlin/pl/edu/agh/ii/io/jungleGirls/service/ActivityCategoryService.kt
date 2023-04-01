package pl.edu.agh.ii.io.jungleGirls.service

import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.repository.ActivityCategoryRepository

@Service
class ActivityCategoryService(private val activityCategoryRepository: ActivityCategoryRepository){
    fun existsById(id: Long):Boolean{
        return activityCategoryRepository.existsById(id).block() ?: false
    }

    fun getIdByName(name: String):Long?{
        return activityCategoryRepository.getIdByName(name).block()
    }
}