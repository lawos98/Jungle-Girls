package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
@Data
@NoArgsConstructor
@AllArgsConstructor
class Permission (
    @Id
    val id:Long,
    val name:String
)
