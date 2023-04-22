package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

@Data
@NoArgsConstructor
@AllArgsConstructor
data class StudentNotification(
    @Id
    val id: Long? = null,
    val date: LocalDateTime,
    val subject: String,
    val content: String,
    val authorId: Long,
    val studentId: Long,
    val wasRead: Boolean,
    )