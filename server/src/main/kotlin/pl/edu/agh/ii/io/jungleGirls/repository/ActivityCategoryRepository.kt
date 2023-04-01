package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.ActivityCategory
import reactor.core.publisher.Mono
@Repository
interface ActivityCategoryRepository : ReactiveCrudRepository<ActivityCategory, Long> {

    override fun findById(id: Long): Mono<ActivityCategory>
    override fun existsById(id: Long): Mono<Boolean>
    fun existsByName(name: String): Mono<Boolean>
    fun getIdByName(name: String): Mono<Long>
}