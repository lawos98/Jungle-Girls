package pl.edu.agh.ii.io.jungleGirls.dto

data class StudentIdWithName(val id: Long, val name:String)
data class ActivityIdWithName(val id: Long,val name:String)
data class ScoreWithActivityIdAndStudentId(val studentId: Long,val activityId:Long, val score: Double){
    fun getKey():Pair<Long,Long>{return Pair(activityId,studentId)}
}
