package pl.edu.agh.ii.io.jungleGirls.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestTempleConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
