package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroupActivity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
interface CourseGroupActivityRepository : ReactiveCrudRepository<CourseGroupActivity, Long> {
    fun findAllByActivityId(activityId: Long): Flux<CourseGroupActivity>
    @Query("select * from course_group_activity where activity_id = :activity_id and course_group_id = :course_group_id")
    fun findByActivityIdAndCourseGroupId(@Param("activity_id") activityId: Long, @Param("course_group_id") courseGroupId: Long): Mono<CourseGroupActivity>

    fun deleteByActivityId(activityId: Long): Flux<Void>
    @Query("update course_group_activity set start_date = :start_date where activity_id = :activity_id and course_group_id = :course_group_id returning *")
    fun updateStartDate(@Param("course_group_id")courseGroupId: Long,@Param("activity_id") activityId: Long,@Param("start_date") startDate: LocalDateTime) : Mono<CourseGroupActivity>
}
