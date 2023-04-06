package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
@Data
@NoArgsConstructor
@AllArgsConstructor
data class PermissionRole(
    @Id
    val roleId: Long? = null,
    val permissionId: Long? = null,
    val shouldBeDisplayed:Boolean
)