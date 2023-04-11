package pl.edu.agh.ii.io.jungleGirls.service

import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.stereotype.Service
import pl.edu.agh.ii.io.jungleGirls.model.LoginUser
import java.lang.Exception
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class TokenService(
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
    private val loginUserService: LoginUserService
) {
    fun createToken(user: LoginUser): String {
        val jwsHeader = JwsHeader.with { "HS256" }.build()
        val claims = JwtClaimsSet.builder()
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(1L, ChronoUnit.HOURS))
            .subject(user.username)
            .claim("userId", user.id)
            .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    fun parseToken(token: String): LoginUser {
        return try {
            val jwt = jwtDecoder.decode(token)
            val userIndex = jwt.claims["userId"] as Long
            loginUserService.findByIndex(userIndex) ?: throw InvalidBearerTokenException(
                "Invalid token"
            )
        } catch (e: Exception) {
            throw e
        }
    }
}
