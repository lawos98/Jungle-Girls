package pl.edu.agh.ii.io.jungleGirls.util

import arrow.core.Either
import arrow.core.None
import arrow.core.left
import arrow.core.right

 fun checkIsBlank(username: String, errorMessage: String): Either<String, None> {
    if (username.isBlank()) {
        return errorMessage.left()
    }
    return None.right()
}