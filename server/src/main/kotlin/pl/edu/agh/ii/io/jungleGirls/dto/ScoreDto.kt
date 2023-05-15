package pl.edu.agh.ii.io.jungleGirls.dto

import pl.edu.agh.ii.io.jungleGirls.model.Activity
import java.time.LocalDateTime

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
    val lastCommitTime: LocalDateTime?,
    val lastCommitError: String?
)

data class ScoreSum(
    val rank: Long,
    val username: String,
    val scoreSum: Double?,
)

data class LeaderboardResponse(
    val username: String,
    val scoreSumList: List<ScoreSum>
)

data class TotalScoreResponse(
    val points: Double,
    val maxPoints: Double
)
