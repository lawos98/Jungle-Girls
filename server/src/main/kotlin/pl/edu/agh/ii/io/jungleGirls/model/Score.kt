package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id

@Data
@NoArgsConstructor
@AllArgsConstructor
data class Score(
    @Id
    val id:Long?=null,
    val studentId:Long,
    val activityId:Long,
    val value:Double?
)