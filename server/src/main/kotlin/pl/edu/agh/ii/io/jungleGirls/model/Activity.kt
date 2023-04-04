package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

@Data
@NoArgsConstructor
@AllArgsConstructor
data class Activity (
    @Id
    var id: Long? = null,
    var name: String,
    var startDate: LocalDateTime,
    var endDate: LocalDateTime,
    var maxScore: Double,
    var description: String,
    var activityTypeId: Long,
    var activityCategoryId: Long,

    )