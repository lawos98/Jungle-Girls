package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id

@Data
@NoArgsConstructor
@AllArgsConstructor
data class CourseGroup(
    @Id
    var id: Long,
    var name: String,
    var instructorId: Long,
    var secretCode: String?
)
