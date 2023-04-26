package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
interface ActivityRepository : ReactiveCrudRepository<Activity,Long> {
    fun findByName(name: String): Mono<Activity>
    fun existsByName(name: String): Mono<Boolean>

    @Query("Insert into activity (name,max_score,duration,description,activity_type_id,activity_category_id) values (:name,:max_score,:duration,:description,:activity_type_id,:activity_category_id) returning *")
    fun save(@Param("name")name: String, @Param("max_score")maxScore: Double, @Param("duration")duration: Duration, @Param("description")description: String, @Param("activity_type_id")activityTypeId: Long, @Param("activity_category_id")activityCategoryId: Long): Mono<Activity>

    @Query("select CASE WHEN COUNT(a.name) > 0 THEN true ELSE false END from activity as a inner join activity_category ac on ac.id = a.activity_category_id where ac.instructor_id = :instructor_id and a.name = :name;")
    fun existsByInstructorIdAndName(@Param("instructor_id")instructorId:Long, @Param("name")name: String): Mono<Boolean>
    @Query("select a.name from activity as a inner join activity_category ac on a.activity_category_id = ac.id where ac.instructor_id = :instructor_id")
    fun getNamesByInstructorId(@Param("instructor_id")instructorId: Long): Flux<String>
    @Query("select a.* from activity as a inner join activity_category ac on a.activity_category_id = ac.id where ac.instructor_id = :instructor_id")
    fun getAllActivityByInstructorId(instructorId: Long): Flux<Activity>
    @Query("select a.id,a.name,a.max_score,a.duration,a.description,a.activity_type_id,a.activity_category_id from activity as a inner join activity_category ac on a.activity_category_id = ac.id where a.name = :name and ac.instructor_id = :instructor_id")
    fun findByInstructorIdAndName(@Param("instructor_id")instructorId:Long,@Param("name")name: String): Mono<Activity>
    @Query("select a.* from activity as a inner join activity_category ac on a.activity_category_id = ac.id where a.id = :activity_id and ac.instructor_id = :instructor_id")
    fun findByIdAndInstructorId(@Param("activity_id")activityId: Long, @Param("instructor_id")instructorId: Long): Mono<Activity>

    @Query("select CASE WHEN COUNT(id) = 0 THEN true ELSE false END from score  where activity_id = :id ")
    fun canBeDeleted(@Param("id") id:Long): Mono<Boolean>

    @Query("Select * from activity a inner join course_group_activity cga on a.id = cga.activity_id where cga.course_group_id=:groupId")
    fun getAllByGroupId(@Param("groupId")groupId:Long): Flux<Activity>
    @Query("update activity  set  name= :name,description = :description, max_score = :max_score, duration = :duration, activity_type_id =:activity_type_id, activity_category_id = :activity_category_id where id = :id returning *")
    fun editActivity(
        @Param("id")activityId: Long,
        @Param("name")name: String,
        @Param("max_score")maxScore: Double,
        @Param("description")description: String,
        @Param("duration")duration: Duration,
        @Param("activity_type_id") activityTypeId: Long,
        @Param("activity_category_id") activityCategoryId: Long,


        ):Mono<Activity>


}
