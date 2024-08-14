package com.jdbenson.domain.api.websocket

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class WebSocketMessageEvent<T> (
    val eventType: String,
    val eventPayload: T
)