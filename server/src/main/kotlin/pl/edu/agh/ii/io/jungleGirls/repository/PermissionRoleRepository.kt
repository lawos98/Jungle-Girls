package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.PermissionRole
import reactor.core.publisher.Flux

@Repository
interface PermissionRoleRepository : ReactiveCrudRepository<PermissionRole,Long>{
    @Query("SELECT pr.role_id,pr.permission_id,pr.should_be_displayed from permission_role pr inner join permission p on p.id = pr.permission_id where pr.role_id=:roleId and pr.should_be_displayed=TRUE")
    fun getPermissionNamesByRoleId(@Param("roleID")roleId:Long):Flux<PermissionRole>
}