package com.fellowflow.userservice.config.security

import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.keycloak.adapters.springsecurity.management.HttpSessionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy


/**
 * @author Nicholas Dietz @ Fellow-Flow
 **/
@KeycloakConfiguration
class KeycloakConfiguration : KeycloakWebSecurityConfigurerAdapter() {
    
    companion object {
        // ORIGINS
        const val ALLOWED_ORIGINS_WILDCARD = "*"

        // HEADERS
        const val ALLOWED_HEADER_AUTHORIZATION = "Authorization"
        const val ALLOWED_HEADER_CACHE_CONTROL = "Cache-Control"
        const val ALLOWED_HEADER_CONTENT_TYPE = "Content-Type"

        // OTHERS
        const val ALLOW_CREDENTIALS = true
    }
    
    @Autowired
    fun configureGlobal(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        val simpleAuthorityMapper = SimpleAuthorityMapper()
        simpleAuthorityMapper.setPrefix("ROLE_")
        val keycloakAuthenticationProvider = keycloakAuthenticationProvider()
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(simpleAuthorityMapper)
        authenticationManagerBuilder.authenticationProvider(keycloakAuthenticationProvider)
    }

    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
    }

    @Bean
    @ConditionalOnMissingBean(HttpSessionManager::class)
    override fun httpSessionManager(): HttpSessionManager {
        return HttpSessionManager()
    }

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        super.configure(httpSecurity)
        httpSecurity.authorizeRequests()
                .antMatchers("*/**").hasRole("ff:user-service:user")
                .anyRequest().permitAll()
    }
}