package pl.edu.agh.ii.io.jungleGirls.service

import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.model.Student
import java.lang.Exception
import java.time.Instant
import java.time.temporal.ChronoUnit


@Service
class TokenService(
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
    private val studentService: StudentService,
) {
    fun createToken(user: Student): String {
        val jwsHeader = JwsHeader.with { "HS256" }.build()
        val claims = JwtClaimsSet.builder()
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(30L, ChronoUnit.DAYS))
            .subject(user.nick)
            .claim("userIndex", user.index)
            .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    fun parseToken(token: String): Student? {
        return try {
            val jwt = jwtDecoder.decode(token)
            val userIndex = jwt.claims["userIndex"] as Long
            studentService.findByIndex(userIndex)
        } catch (e: Exception) {
            null
        }
    }
}