package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.Role
import reactor.core.publisher.Mono

@Repository
interface RoleRepository: ReactiveCrudRepository<Role, Long> {
    @Query("Select r.id,r.name,r.description,r.secret_code from role r inner join login_user lu on r.id = lu.role_id where lu.id = :userId")
    fun findByUserId(@Param("userId")userId: Long): Mono<Role>
}
