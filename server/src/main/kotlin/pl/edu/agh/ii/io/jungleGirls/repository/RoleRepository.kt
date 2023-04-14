package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.Role

@Repository
interface RoleRepository: ReactiveCrudRepository<Role, Long> {

}