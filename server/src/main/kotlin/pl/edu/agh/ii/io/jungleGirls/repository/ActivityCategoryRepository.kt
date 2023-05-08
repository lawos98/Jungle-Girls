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

    @Query("Insert into activity_category (name,description,instructor_id) VALUES (:name,:description,:instructorId) returning *")
    fun save(@Param("name")name: String,@Param("description")description: String,@Param("instructorId")instructorId: Long): Mono<ActivityCategory>

    @Query(
        "select CASE WHEN COUNT(name) > 0 THEN true ELSE false END from activity_category where instructor_id = :instructor_id and name = :name;"
    )
    fun existsByInstructorIdAndName(
        @Param("instructor_id")instructorId: Long,
        @Param("name")name: String
    ): Mono<Boolean>

    @Query(
        "select id from activity_category where instructor_id = :instructor_id and name = :name;"
    )
    fun getIdByInstructorIdAndName(
        @Param("instructor_id")instructorId: Long,
        @Param("name")name: String
    ): Mono<Long>

    fun findByInstructorIdAndName(
        @Param("instructor_id")instructorId: Long,
        @Param("name")name: String
    ): Mono<ActivityCategory>

    @Query("select name from activity_category where instructor_id = :id;")
    fun findAllNamesById(@Param("id") id: Long): Flux<String>

    fun findByIdAndInstructorId(activityCategoryId: Long, instructorId: Long): Mono<ActivityCategory>


    @Query(
        "select CASE WHEN COUNT(id) = 0 THEN true ELSE false END from activity  where activity_category_id = :id "
    )
    fun canBeDeleted(@Param("id") id: Long): Mono<Boolean>
    @Query("select * from activity_category where instructor_id = :id;")
    fun getAllActivityCategoriesByInstructorId(instructorId: Long): Flux<ActivityCategory>

    @Query("select name from activity_category where id = :id and instructor_id = :instructor_id")
    fun getNameByIdAndInstructorId(@Param("id") id: Long,@Param("instructor_id")instructorId: Long): Mono<String>
    @Query("update activity_category set name = :name, description = :description where id = :id returning *")
    fun update(@Param("id") id: Long,@Param("name") name: String,@Param("description") description: String): Mono<ActivityCategory>
}
