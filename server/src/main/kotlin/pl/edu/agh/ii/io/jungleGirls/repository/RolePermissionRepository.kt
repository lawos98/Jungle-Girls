package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.RolePermission
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface RolePermissionRepository : ReactiveCrudRepository<RolePermission,Long>{
    @Query("SELECT pr.role_id,pr.permission_id,pr.should_be_displayed from role_permission pr inner join permission p on p.id = pr.permission_id where pr.role_id=:roleId and pr.should_be_displayed=TRUE")
    fun getPermissionNamesByRoleId(@Param("roleID")roleId:Long):Flux<RolePermission>

    @Query("UPDATE role_permission rp SET should_be_displayed=:should_be_displayed WHERE rp.role_id=:roleId and rp.permission_id=:permissionId RETURNING *")
    fun updateRolePermission(@Param("roleId")roleId:Long,@Param("permissionId")permissionId:Long,@Param("shouldBeDisplayed")shouldBeDisplayed:Boolean):Mono<RolePermission>
}