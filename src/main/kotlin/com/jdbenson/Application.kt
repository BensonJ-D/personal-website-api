package com.jdbenson

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.jdbenson.plugins.configureCallLogging
import com.jdbenson.plugins.configureDependencyInjection
import com.jdbenson.plugins.configureWebSockets
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import org.litote.kmongo.id.jackson.IdJacksonModule
import com.auth0.jwk.JwkProviderBuilder
import com.jdbenson.resources.SseEvent
import io.ktor.network.tls.certificates.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun validateCreds(credential: JWTCredential, permission: String? = null): JWTPrincipal? {
    val containsAudience = credential.payload.audience.contains(System.getenv("AUDIENCE"))
    val containsScope = permission.isNullOrBlank() ||
            credential.payload.claims["permissions"]?.asArray(String::class.java)?.contains(permission) == true

    if (containsAudience && containsScope) {
        return JWTPrincipal(credential.payload)
    }

    return null
}

fun Application.module() {
    val jwkProvider = JwkProviderBuilder(System.getenv("DOMAIN"))
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    install(Authentication) {
        jwt("read-auth") {
            verifier(jwkProvider, System.getenv("DOMAIN"))
            validate { credential -> validateCreds(credential, "read:content") }
        }

        jwt("post-auth") {
            verifier(jwkProvider, System.getenv("DOMAIN"))
            validate { credential -> validateCreds(credential, "post:content") }
        }
    }

    install(ContentNegotiation) {
        jackson {
            this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            this.dateFormat = StdDateFormat().withColonInTimeZone(true)
            this.registerModule(kotlinModule())
            this.registerModule(IdJacksonModule())
        }
    }

    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)

        allowMethod(HttpMethod.Options)

        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.AccessControlAllowMethods)
        allowHeader(HttpHeaders.ContentType)

        allowHeader(HttpHeaders.Authorization)
        allowCredentials = true
        allowNonSimpleContentTypes = true

        anyHost()
    }

    configureCallLogging()
    configureWebSockets()

    // Must do last to make sure all ktor modules are available for resources
    configureDependencyInjection()
}
