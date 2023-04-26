package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.Score
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface ScoreRepository: ReactiveCrudRepository<Score, Long> {

    @Query("Select s.id,s.student_id,s.activity_id,s.value from score s inner join student_description sd  on sd.id=s.student_id  where sd.course_group_id = :groupId")
    fun getScoresByGroupId(@Param("groupId") groupId: Long): Flux<Score>

    @Query("Update score set value=:value where id = :scoreId RETURNING *")
    fun updateScoreById(@Param("scoreId")scoreId: Long, @Param("value")value: Double): Mono<Score>

    @Query("Select s.id,s.student_id,s.activity_id,s.value from course_group_activity cga inner join activity a on cga.activity_id = a.id inner join score s on a.id = s.activity_id where cga.course_group_id=:groupId and s.student_id = :studentId and s.activity_id=:activityId")
    fun getScore(@Param("activityId")activityId: Long, @Param("studentId")studentId:Long, @Param("groupId")groupId:Long) : Mono<Score?>

    @Query("Select id,student_id,activity_id,value from score where student_id=:studentId")
    fun getScoreByStudentId(@Param("studentId")studentId:Long) : Flux<Score?>

    @Query("Insert into score (student_id,activity_id,value) values (:studentId,:activity,:value) returning *")
    fun save(@Param("studentId")studentId:Long, @Param("activity")activityId:Long, @Param("value")value:Double) : Mono<Score>
}
