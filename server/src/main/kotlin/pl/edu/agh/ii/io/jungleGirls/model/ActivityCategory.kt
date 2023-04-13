package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

@Data
@NoArgsConstructor
@AllArgsConstructor
data class ActivityCategory (
    @Id
    var id: Long? = null,
    var name: String,
    var description: String,
    var instructorId: Long,
    )