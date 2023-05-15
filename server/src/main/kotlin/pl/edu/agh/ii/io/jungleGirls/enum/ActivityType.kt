package pl.edu.agh.ii.io.jungleGirls.enum
enum class ActivityType {
    COMPULSORY,
    OPTIONAL,
    REPARATIVE,
    TEMPORARY_EVENT;

    fun getId():Long{
        return when (this){
            COMPULSORY -> 1
            OPTIONAL -> 2
            REPARATIVE -> 3
            TEMPORARY_EVENT -> 4
        }
    }
}
