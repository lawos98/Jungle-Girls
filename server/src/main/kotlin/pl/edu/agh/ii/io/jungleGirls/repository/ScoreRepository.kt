package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.dto.ScoreSum
import pl.edu.agh.ii.io.jungleGirls.dto.StudentIdWithScore
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

    @Query("select lu.username, row_number() over(order by COALESCE(sum(s.value), -1)  desc) as rank, sum(s.value)  as score_sum  from login_user lu inner join student_description sd on lu.id = sd.id left outer join score s on lu.id = s.student_id where sd.course_group_id = :groupId group by lu.id")
    fun getScoreSumList(@Param("groupId") groupId: Long):Flux<ScoreSum>
    @Query("select sd.id as student_id, s.value  as value from student_description sd left join score s on sd.id = s.student_id where sd.course_group_id = :groupId and s.activity_id = :activityId union select sd.id as student_id, 0 as value from student_description sd where sd.course_group_id = :groupId and not exists (select 1 from score as s where s.student_id = sd.id and s.activity_id = :activityId);")
    fun getStudentIdsAndScoresVyActivityIdAndGroupId(@Param("groupId") groupId: Long,@Param("activityId") activityId: Long):Flux<StudentIdWithScore>
}
