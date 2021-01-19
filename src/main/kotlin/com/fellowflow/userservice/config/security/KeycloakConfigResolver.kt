package com.fellowflow.userservice.config.security

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * @author Nicholas Dietz @ Fellow-Flow
 **/
@Configuration
class KeycloakConfigResolver {

    @Bean
    fun keycloakSpringBootConfigResolver(): KeycloakSpringBootConfigResolver {
        return KeycloakSpringBootConfigResolver()
    }

}
