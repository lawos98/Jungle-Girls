package pl.edu.agh.ii.io.jungleGirls.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Data
@NoArgsConstructor
@AllArgsConstructor
data class CourseGroupActivity (

    var courseGroupId: Long,

    var activityId: Long
)

//class GroupActivityId(private var groupId: Long,private var activityId: Long)