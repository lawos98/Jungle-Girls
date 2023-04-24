package pl.edu.agh.ii.io.jungleGirls.enum
enum class Roles {
    UNAUTHORIZED,
    STUDENT,
    LECTURER,
    COORDINATOR;

    fun getId():Long{
        return when (this){
            UNAUTHORIZED -> 1
            STUDENT -> 2
            LECTURER -> 3
            COORDINATOR -> 4
        }
    }
}
