package pl.edu.agh.ii.io.jungleGirls.service

import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroupActivity
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import pl.edu.agh.ii.io.jungleGirls.repository.CourseGroupActivityRepository
@Service
class CourseGroupActivityService (
    private val courseGroupActivityRepository: CourseGroupActivityRepository
){
    fun getAllByActivityId(activityId:Long):List<CourseGroupActivity>{
        return courseGroupActivityRepository.findAllByActivityId(activityId).collectList().block() as ArrayList<CourseGroupActivity>
    }
}
