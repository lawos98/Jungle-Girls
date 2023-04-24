package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.edu.agh.ii.io.jungleGirls.model.StudentDescription
import reactor.core.publisher.Mono

interface StudentDescriptionRepository :ReactiveCrudRepository<StudentDescription, Long> {


    @Query("Update student_description set index = :index , github_link = :githubLink where id = :id returning *")
    fun updateDescription(@Param("id") id:Long,@Param("index") index:Long?, @Param("githubLink") githubLink:String?):Mono<StudentDescription>
}
