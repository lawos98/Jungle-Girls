package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.Activity
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import reactor.core.publisher.Mono

@Repository
interface ActivityRepository : ReactiveCrudRepository<Activity,Long> {
    fun findByName(name: String): Mono<Activity>
    fun existsByName(name: String): Mono<Boolean>
}