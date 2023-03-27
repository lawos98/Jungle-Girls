package pl.edu.agh.ii.io.jungleGirls.config

import org.springframework.security.core.Authentication
import pl.edu.agh.ii.io.jungleGirls.model.Student

fun Authentication.toUser(): Student {
    return principal as Student
}