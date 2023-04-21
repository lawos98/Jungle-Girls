package pl.edu.agh.ii.io.jungleGirls.util

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
object Bcrypt {
    fun checkBcrypt(input: String, hash: String): Boolean {
        return BCrypt.checkpw(input, hash)
    }
    fun hashBcrypt(input: String): String {
        return BCrypt.hashpw(input, BCrypt.gensalt(10))
    }
}
