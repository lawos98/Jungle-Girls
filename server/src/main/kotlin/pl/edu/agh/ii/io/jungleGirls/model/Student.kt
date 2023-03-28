package pl.edu.agh.ii.io.jungleGirls.model


import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
@Data
@NoArgsConstructor
@AllArgsConstructor
data class Student(
    @Id val index: Long,
    val nick: String,
    val password: String,
    val github_link: String="",
)