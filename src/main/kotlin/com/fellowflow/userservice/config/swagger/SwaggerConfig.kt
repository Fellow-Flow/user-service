package com.fellowflow.userservice.config.swagger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.Contact
import springfox.documentation.service.GrantType
import springfox.documentation.service.OAuth
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant
import springfox.documentation.service.SecurityReference
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger.web.SecurityConfigurationBuilder
import kotlin.random.Random

/**
 * @author Nicholas Dietz @ Fellow-Flow
 *
 * Swagger configuration @see http://<host>:[application.yml:server.port]/swagger-ui.html#
 * Keep in mind that you have to authorize yourself to access any endpoint!
 **/
@Profile("default")
@Configuration
class SwaggerConfig {

    @Value("\${keycloak.auth-server-url}")
    private val keycloakAuthServerUrl: String? = null

    @Value("\${keycloak.realm}")
    private val keycloakRealm: String? = null

    @Bean
    fun api(): Docket? {
        return Docket(DocumentationType.SWAGGER_2).select()
            .apis(RequestHandlerSelectors.basePackage("com.fellowflow.userservice.controller"))
            .paths(PathSelectors.any())
            .build()
            .securityContexts(listOf(securityContext()))
            .securitySchemes(listOf(securitySchema()))
            .apiInfo(getApiInformation())
    }

    private fun getApiInformation(): ApiInfo {
        return ApiInfo(
            "Fellow-Flow User Service",
            null,
            null,
            null,
            Contact("Nicholas Dietz", "https://github.com/nicos-dev", ""),
            null,
            null,
            emptyList()
        )
    }

    @Autowired
    fun apiKey(): SecurityScheme {
        return ApiKey(HttpHeaders.AUTHORIZATION, "apiKey", "header")
    }

    private fun securitySchema(): OAuth {
        val authorizationScopeList: MutableList<AuthorizationScope> = mutableListOf()
        authorizationScopeList.add(AuthorizationScope("read", "read all"))
        authorizationScopeList.add(AuthorizationScope("write", "access all"))
        val grantTypes: MutableList<GrantType> = mutableListOf()
        val passwordCredentialsGrant: GrantType = ResourceOwnerPasswordCredentialsGrant(
            "$keycloakAuthServerUrl/realms/$keycloakRealm/protocol/openid-connect/token"
        )
        grantTypes.add(passwordCredentialsGrant)
        return OAuth("oauth2", authorizationScopeList, grantTypes)
    }

    private fun securityContext(): SecurityContext? {
        return SecurityContext.builder().securityReferences(defaultAuth())
            .build()
    }

    private fun defaultAuth(): List<SecurityReference?> {
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(3)
        authorizationScopes[0] = AuthorizationScope("read", "read all")
        authorizationScopes[1] = AuthorizationScope("trust", "trust all")
        authorizationScopes[2] = AuthorizationScope("write", "write all")
        return listOf(SecurityReference("oauth2", authorizationScopes))
    }

    @Bean
    fun security(): SecurityConfiguration? {
        val additionalQueryStringParams: MutableMap<String, Any> = HashMap()
        additionalQueryStringParams["nonce"] = Random.nextInt(100000, 999999)
        return SecurityConfigurationBuilder.builder()
            .clientId("swagger").realm(keycloakRealm).appName("user-service-swagger-ui")
            .additionalQueryStringParams(additionalQueryStringParams)
            .build()
    }
}
