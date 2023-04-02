package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.CourseGroup
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
@Repository
interface CourseGroupRepository : ReactiveCrudRepository<CourseGroup,Long> {

    override fun findById(id: Long): Mono<CourseGroup>
    override fun existsById(id: Long): Mono<Boolean>
    fun existsByName(name: String): Mono<Boolean>

    fun getIdByName(name: String): Mono<Long>
    @Query("select name from course_group;")
    fun findAllNames(): Flux<String>
}