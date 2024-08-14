package com.jdbenson.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.util.*

fun Application.configureCallLogging() {
    install(CallLogging) {
        format { call ->
            val path = call.request.uri
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            val time = call.processingTimeMillis()
            "$httpMethod $path | Status: $status | User agent: $userAgent | Duration (ms): $time"
        }
    }
}