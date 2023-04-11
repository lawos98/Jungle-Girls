package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Data
@NoArgsConstructor
@AllArgsConstructor
data class CourseGroupActivity (

    var courseGroupId: Long,
    var activityId: Long,
    var startDate: LocalDateTime
)

//class GroupActivityId(private var groupId: Long,private var activityId: Long)