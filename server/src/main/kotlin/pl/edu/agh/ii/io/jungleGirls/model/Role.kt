package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
@Data
@NoArgsConstructor
@AllArgsConstructor
data class Role(

    @Id
    val id: Long? = null,
    val name: String,
    val description: String,
    var secretCode: String,
)