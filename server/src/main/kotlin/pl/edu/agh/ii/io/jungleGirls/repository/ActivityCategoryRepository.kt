package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.ActivityCategory
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
@Repository
interface ActivityCategoryRepository : ReactiveCrudRepository<ActivityCategory, Long> {

    override fun findById(id: Long): Mono<ActivityCategory>
    override fun existsById(id: Long): Mono<Boolean>
    fun existsByName(name: String): Mono<Boolean>
    @Query("select id from activity_category where instructor_id = :instructor_id and name = :name;")
    fun getIdByInstructorIdAndName(@Param("instructor_id")instructorId:Long,@Param("name")name: String): Mono<Long>

    @Query("select name from activity_category where instructor_id = :id;")
    fun findAllNamesById(@Param("id") id:Long): Flux<String>
}