package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id

@Data
@NoArgsConstructor
@AllArgsConstructor
data class LoginUser(
    @Id
    val id: Long? = null,
    var username: String,
    var password: String,
    var firstname: String,
    var lastname: String,
    var roleId: Long
)
