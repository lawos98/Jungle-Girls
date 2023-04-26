package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import reactor.core.publisher.Mono

@Repository
interface LoginUserRepository : ReactiveCrudRepository<LoginUser, Long> {
    fun findByUsername(username: String): Mono<LoginUser>
    override fun findById(id: Long): Mono<LoginUser>

    @Query("UPDATE login_user lu SET role_id=:roleId WHERE lu.id=:userId RETURNING *")
    fun updateUserRole(@Param("roleId")roleId:Long, @Param("userId")userId:Long):Mono<LoginUser>

    @Query("Insert into login_user (username,password,firstname,lastname,role_id) VALUES (:username,:password,:first_name,:last_name,:roleId) returning *")
    fun save(@Param("username")username: String, @Param("password")password: String, @Param("first_name")firstName: String, @Param("last_name")lastName: String, @Param("roleId")roleId: Long): Mono<LoginUser>
}
