package pl.edu.agh.ii.io.jungleGirls.config

import org.springframework.security.core.Authentication
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser

fun Authentication.toUser(): LoginUser {
    return principal as LoginUser
}
