package com.jdbenson.domain.api.websocket

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class WebSocketMessage<PayloadType> (
    val messageType: WebSocketMessageType,
    val messagePayload: PayloadType
)