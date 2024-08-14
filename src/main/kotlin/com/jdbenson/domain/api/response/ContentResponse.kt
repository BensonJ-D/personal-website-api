package com.jdbenson.domain.api.response

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.jdbenson.serializers.OffsetDateTimeSerializer
import java.time.OffsetDateTime

data class ContentResponse(
    val pageTitle: String,
    val content: String,
    @JsonSerialize(using = OffsetDateTimeSerializer::class)
    val timestamp: OffsetDateTime = OffsetDateTime.now()
)