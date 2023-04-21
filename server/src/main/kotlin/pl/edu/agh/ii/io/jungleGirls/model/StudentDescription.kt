package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id

@Data
@NoArgsConstructor
@AllArgsConstructor
data class StudentDescription(
    @Id
    val id: Long,
    var index: Long?,
    var githubLink: String?,
    var courseGroupId: Long?
)