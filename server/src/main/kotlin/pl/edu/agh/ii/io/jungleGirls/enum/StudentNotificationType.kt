package pl.edu.agh.ii.io.jungleGirls.enum

enum class StudentNotificationType() {
    NEW_SCORE,
    CHANGED_SCORE,
    DELETED_SCORE;

    fun getSubject():String{
        return when (this){
            NEW_SCORE -> "New score notification."
            CHANGED_SCORE -> "Score changed notification."
            DELETED_SCORE-> "Score deleted notification."
        }
    }

    fun getContent(activityName: String, score: Double?,maxScore: Double):String{
        return when (this){
            NEW_SCORE -> "You received $score/$maxScore points for the activity \"$activityName\"."
            CHANGED_SCORE -> "Your score for the activity \"$activityName\" was changed to $score points."
            DELETED_SCORE-> "Your score received for the activity \"$activityName\" was deleted."
        }
    }


}