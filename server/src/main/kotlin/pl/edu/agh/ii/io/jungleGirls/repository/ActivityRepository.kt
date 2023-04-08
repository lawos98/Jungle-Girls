package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import reactor.core.publisher.Mono

@Repository
interface ActivityRepository : ReactiveCrudRepository<Activity,Long> {
    fun findByName(name: String): Mono<Activity>
    fun existsByName(name: String): Mono<Boolean>

    @Query("select CASE WHEN COUNT(a.name) > 0 THEN true ELSE false END from activity as a inner join activity_category ac on ac.id = a.activity_category_id where ac.instructor_id = :instructor_id and a.name = :name;")
    fun existsByInstructorIdAndName(@Param("instructor_id")instructorId:Long, @Param("name")name: String): Mono<Boolean>

}