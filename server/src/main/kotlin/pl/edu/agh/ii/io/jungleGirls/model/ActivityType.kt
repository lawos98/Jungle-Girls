package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id

@Data
@NoArgsConstructor
@AllArgsConstructor
data class ActivityType (
    @Id
    var id: Long? = null,
    var name: String,
)