package com.jdbenson.plugins

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import java.time.Duration

fun Application.configureWebSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = JacksonWebsocketContentConverter()
    }
}