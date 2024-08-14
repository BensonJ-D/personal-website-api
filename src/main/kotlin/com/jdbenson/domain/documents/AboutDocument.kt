package com.jdbenson.domain.documents

import com.jdbenson.domain.api.request.AboutRequest
import com.jdbenson.domain.api.response.AboutResponse
import org.bson.types.ObjectId
import java.time.OffsetDateTime
import java.time.ZoneOffset

data class AboutDocument(
    val _id: ObjectId? = null,
    val content: String,
    val timestamp: OffsetDateTime,
    val version: Int = 1
) {
    fun toResponse() = AboutResponse(content = content, timestamp = timestamp)

    companion object {
        fun fromRequest(request: AboutRequest) = AboutDocument(content = request.content, timestamp = request.timestamp.withOffsetSameInstant(ZoneOffset.UTC))
    }
}