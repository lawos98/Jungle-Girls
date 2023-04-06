package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.Role
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface RoleRepository: ReactiveCrudRepository<Role, Long> {
    override fun findAll():Flux<Role>
    fun findByName(name:String): Mono<Role>
}