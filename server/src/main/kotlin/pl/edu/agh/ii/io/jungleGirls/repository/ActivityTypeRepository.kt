package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.ActivityCategory
import pl.edu.agh.ii.io.jungleGirls.model.ActivityType
import reactor.core.publisher.Mono
@Repository
interface ActivityTypeRepository : ReactiveCrudRepository<ActivityType, Long> {

    override fun findById(id: Long): Mono<ActivityType>
    fun getIdByName (name: String): Mono<Long>
    override fun existsById(id: Long): Mono<Boolean>
     fun existsByName(name: String): Mono<Boolean>


}