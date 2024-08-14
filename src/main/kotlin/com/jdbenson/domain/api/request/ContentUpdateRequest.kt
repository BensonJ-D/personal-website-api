package com.jdbenson.domain.api.request

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.OffsetDateTime

sealed interface ContentRequest {
    val route: String
    val pageTitle: String
    val content: String

    val timestamp: OffsetDateTime
}

data class ContentNewRequest(
    override val route: String,
    override val pageTitle: String,
    override val content: String,

    @JsonIgnore
    override val timestamp: OffsetDateTime = OffsetDateTime.now()
) : ContentRequest

data class ContentUpdateRequest(
    override val route: String,
    override val pageTitle: String,
    override val content: String,

    @JsonIgnore
    override val timestamp: OffsetDateTime = OffsetDateTime.now()
) : ContentRequest



