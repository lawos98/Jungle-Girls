package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.edu.agh.ii.io.jungleGirls.model.StudentNotification
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface StudentNotificationRepository: ReactiveCrudRepository<StudentNotification, Long> {
        @Query("select * from student_notification where student_id = :student_id order by date desc ")
        fun findAllByStudentId(@Param("student_id")studentId:Long): Flux<StudentNotification>
        @Query("update student_notification  set was_read = true where id = :id and student_id =:student_id")
       fun updateWasRead(@Param("id")id:Long,@Param("student_id")studentId: Long): Mono<StudentNotification>
}