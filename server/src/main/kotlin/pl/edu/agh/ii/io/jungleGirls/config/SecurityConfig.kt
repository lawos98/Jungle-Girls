package pl.edu.agh.ii.io.jungleGirls.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import pl.edu.agh.ii.io.jungleGirls.enum.Permissions
import pl.edu.agh.ii.io.jungleGirls.service.RolePermissionService
import pl.edu.agh.ii.io.jungleGirls.service.PermissionService
import pl.edu.agh.ii.io.jungleGirls.service.TokenService

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val tokenService: TokenService,
    private val rolePermissionService: RolePermissionService,
    private val permissionService: PermissionService
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
            .requestMatchers(HttpMethod.PATCH,"api/role-permission").permitAll()
            .requestMatchers(HttpMethod.PUT, "/api/role-permission").hasAuthority(Permissions.USERS_MANAGEMENT.permissionName)
            .requestMatchers(HttpMethod.GET,"/api/role/secret-code/{id}").hasAuthority(Permissions.USERS_MANAGEMENT.permissionName)
            .requestMatchers(HttpMethod.PUT, "/api/permission").hasAuthority(Permissions.USERS_MANAGEMENT.permissionName)
//            .requestMatchers(HttpMethod.GET,"/api/student_notification").hasAuthority()
//            .requestMatchers(HttpMethod.PUT,"/api/activity_category").hasAuthority()
//            .requestMatchers(HttpMethod.GET,"/api/activity_category").hasAuthority()
//            .requestMatchers(HttpMethod.PUT,"/api/activity").hasAuthority()
//            .requestMatchers(HttpMethod.GET,"/api/activity").hasAuthority()
            .requestMatchers("/api/**").authenticated()
            .anyRequest().permitAll()


        http.oauth2ResourceServer().jwt()
        http.authenticationManager { auth ->
            val jwt = auth as BearerTokenAuthenticationToken
            val user = tokenService.parseToken(jwt.token)
            val authorities = rolePermissionService.getPermissionNamesByRoleId(user.roleId)
                .map { SimpleGrantedAuthority(permissionService.findById(it.permissionId!!).name) }
            UsernamePasswordAuthenticationToken(user, "", authorities)
        }

        http.cors()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.csrf().disable()
        http.headers().frameOptions().disable()
        http.headers().xssProtection().disable()

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000", "http://localhost:5173")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("authorization", "content-type")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
