package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroupActivity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface CourseGroupActivityRepository : ReactiveCrudRepository<CourseGroupActivity, Long> {
    fun findAllByActivityId(activityId: Long): Flux<CourseGroupActivity>
    fun findByActivityIdAndCourseGroupId(activityId: Long, courseGroupId: Long): Mono<CourseGroupActivity>

    fun deleteByActivityId(activityId: Long): Flux<Void>
}
