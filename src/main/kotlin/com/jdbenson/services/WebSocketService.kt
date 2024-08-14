package com.jdbenson.services

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.jdbenson.domain.Connection
import com.jdbenson.domain.api.websocket.WebSocketMessage
import com.jdbenson.domain.api.websocket.WebSocketMessageEvent
import com.jdbenson.domain.api.websocket.WebSocketMessageType
import io.ktor.websocket.*
import java.util.*
import kotlin.collections.LinkedHashSet

class WebSocketService {
    private val connections: MutableSet<Connection?> = Collections.synchronizedSet(LinkedHashSet())

    fun addConnection(connection: Connection) {
        connections += connection
    }

    fun removeConnection(connection: Connection) {
        connections -= connection
    }

    suspend fun sendAll(content: Any) {
        connections.forEach {
            it?.session?.sendEvent(eventPayload = content)
        }
    }

    suspend fun send(connection: Connection, content: Any, eventType: String? = null) {
        connection.session.sendEvent(eventPayload = content, eventType = eventType)
    }

    private suspend fun DefaultWebSocketSession.sendEvent(eventPayload: Any, eventType: String? = null) {
        val type = eventType ?: eventPayload.javaClass.simpleName
        val webSocketMessageEvent = WebSocketMessageEvent(type, eventPayload)
        val webSocketMessage = WebSocketMessage(WebSocketMessageType.EVENT, webSocketMessageEvent)

        val jsonString = jsonMapper().writeValueAsString(webSocketMessage)
        println(jsonString)

        this.send(jsonString)
    }
}

