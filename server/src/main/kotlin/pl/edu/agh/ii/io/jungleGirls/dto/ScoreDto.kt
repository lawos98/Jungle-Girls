package pl.edu.agh.ii.io.jungleGirls.dto

import pl.edu.agh.ii.io.jungleGirls.model.Activity

data class ActivityScoreList(
    val activity: Activity,
    var students:List<StudentScore>,
)

data class ActivityScore(
    val activity: Activity,
    val values: Double?
)

data class StudentScore(
    val id:Long,
    val username: String,
    val firstname: String,
    val lastname: String,
    var value: Double?,
)