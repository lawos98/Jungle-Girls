package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.Permission
import reactor.core.publisher.Mono

@Repository
interface PermissionRepository: ReactiveCrudRepository<Permission, Long> {
    override fun findById(id:Long): Mono<Permission>

    override fun existsById(id: Long): Mono<Boolean>
}