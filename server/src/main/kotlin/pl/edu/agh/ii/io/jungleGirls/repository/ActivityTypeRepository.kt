package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.ActivityType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
@Repository
interface ActivityTypeRepository : ReactiveCrudRepository<ActivityType, Long> {

    override fun findById(id: Long): Mono<ActivityType>
    fun getIdByName(name: String): Mono<Long>
    override fun existsById(id: Long): Mono<Boolean>
    fun existsByName(name: String): Mono<Boolean>

    @Query("select name from activity_type;")
    fun findAllNames(): Flux<String>
    @Query("select name from activity_type where id = :id")
    fun getNameById(@Param("id") activityTypeId: Long): Mono<String>
}
