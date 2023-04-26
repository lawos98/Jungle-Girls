package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id

@Data
@NoArgsConstructor
@AllArgsConstructor
data class RolePermission(
    @Id
    val roleId: Long,
    val permissionId: Long,
    val shouldBeDisplayed: Boolean
)
