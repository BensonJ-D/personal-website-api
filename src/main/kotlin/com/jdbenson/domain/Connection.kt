package com.jdbenson.domain

import io.ktor.websocket.*
import java.util.concurrent.atomic.*

class Connection(public val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }

    val id = lastId.getAndIncrement()
}