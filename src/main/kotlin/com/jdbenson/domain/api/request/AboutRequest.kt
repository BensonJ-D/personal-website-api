package com.jdbenson.domain.api.request

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.OffsetDateTime

data class AboutRequest(
    val content: String,

    @JsonIgnore
    val timestamp: OffsetDateTime = OffsetDateTime.now()
)