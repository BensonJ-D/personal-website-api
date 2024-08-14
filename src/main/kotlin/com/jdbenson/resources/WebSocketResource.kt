package com.jdbenson.resources
import com.google.inject.Inject
import com.jdbenson.domain.Connection
import com.jdbenson.services.WebSocketService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.utils.io.*
import io.ktor.websocket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.yield
import kotlin.time.Duration.Companion.seconds

data class SseEvent(val data: String, val event: String? = null, val id: String? = null)

suspend fun ApplicationCall.respondSse(eventFlow: Flow<SseEvent>) {
    response.cacheControl(CacheControl.NoCache(null))
    respondBytesWriter(contentType = ContentType.Text.EventStream) {
        eventFlow.collect { event ->
            if (event.id != null) {
                writeStringUtf8("id: ${event.id}\n")
            }
            if (event.event != null) {
                writeStringUtf8("event: ${event.event}\n")
            }
            for (dataLine in event.data.lines()) {
                writeStringUtf8("data: $dataLine\n")
            }
            writeStringUtf8("\n")
            flush()
        }
    }
}

class WebSocketResource @Inject constructor(application: Application,
                                            webSocketService: WebSocketService
) {
    init {
        val sseFlow = flow {
            var n = 0
            while (true) {
                emit(SseEvent("demo$n"))
                delay(1.seconds)
                n++
            }
        }.shareIn(GlobalScope, SharingStarted.Eagerly)

        application.routing {
            authenticate("read-auth") {
                webSocket("/chat") {
                    val connection = Connection(this)
                    webSocketService.addConnection(connection)

                    // Yield to allow the connection to fully open
                    yield()

                    try {
                        webSocketService.send(connection, "Connected to backend")
                        for (frame in incoming) {
                            frame as? Frame.Text ?: continue
                            val receivedText = frame.readText()
                            webSocketService.send(connection, "You said: $receivedText")
                        }
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                    } finally {
                        println("Removing $connection!")
                        webSocketService.removeConnection(connection)
                    }
                }
            }

            get("/sse") {
                call.respondSse(sseFlow)
            }
        }
    }
}
