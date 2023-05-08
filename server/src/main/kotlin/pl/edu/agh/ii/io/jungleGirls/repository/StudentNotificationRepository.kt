package pl.edu.agh.ii.io.jungleGirls.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.edu.agh.ii.io.jungleGirls.model.StudentNotification
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface StudentNotificationRepository: ReactiveCrudRepository<StudentNotification, Long> {
        @Query("select * from student_notification where student_id = :student_id order by date desc ")
        fun findAllByStudentId(@Param("student_id")studentId:Long): Flux<StudentNotification>
        @Query("update student_notification  set was_read = true where id = :student_notification_id and student_id =:student_id returning *")
        fun updateWasRead(@Param("student_id")studentId: Long, @Param("student_notification_id")studentNotificationId:Long): Mono<StudentNotification>

        @Query("Insert into Student_notification (date, subject, content, author_id, student_id, was_read) values (:date, :subject, :content, :author_id, :student_id, :was_read) returning *")
        fun save(@Param("subject")subject: String, @Param("content")content: String,@Param("date")date:LocalDateTime, @Param("author_id")authorId: Long, @Param("student_id")studentId: Long, @Param("was_read")wasRead: Boolean): Mono<StudentNotification>

}
