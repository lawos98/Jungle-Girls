package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroup
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
@Repository
interface CourseGroupRepository : ReactiveCrudRepository<CourseGroup,Long> {

    override fun findById(id: Long): Mono<CourseGroup>
    override fun existsById(id: Long): Mono<Boolean>
    fun existsByName(name: String): Mono<Boolean>

    fun getIdByName(name: String): Mono<Long>
    @Query("select name from course_group where instructor_id = :id;")
    fun findAllNamesById(@Param("id") id:Long): Flux<String>

    @Query("Select id from course_group where instructor_id = :instructorId and id=:groupId")
    fun checkLecturerGroup(@Param("instructorId") instructorId:Long, @Param("groupId") groupId:Long): Flux<Long>

    @Query("Select lu.id,lu.username,lu.password,lu.firstname,lu.lastname,lu.role_id from login_user lu inner join student_description sd on lu.id = sd.id where sd.course_group_id = :groupId")
    fun getAllStudentsByGroupId(@Param("groupId") groupId:Long): Flux<LoginUser>
    @Query("select name from course_group where id = :groupId")
    fun findNameById(@Param("groupId")groupId: Long): Mono<String>

    @Query("select id,name,instructor_id,secret_code from course_group where instructor_id = :instructorId")
    fun getAllGroups(@Param("instructorId") instructorId:Long): Flux<CourseGroup>
}
