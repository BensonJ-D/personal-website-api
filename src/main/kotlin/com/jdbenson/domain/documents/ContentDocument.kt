package com.jdbenson.domain.documents

import com.jdbenson.domain.api.request.ContentRequest
import com.jdbenson.domain.api.request.ContentUpdateRequest
import com.jdbenson.domain.api.response.ContentResponse
import org.bson.types.ObjectId
import java.time.OffsetDateTime
import java.time.ZoneOffset

data class ContentDocument(
    val _id: ObjectId? = null,
    val route: String,
    val pageTitle: String,
    val content: String,
    val timestamp: OffsetDateTime,
    val version: Int = 1
) {
    fun toResponse() = ContentResponse(pageTitle = pageTitle, content = content, timestamp = timestamp)

    companion object {
        fun fromRequest(request: ContentRequest) =
            ContentDocument(
                route = request.route,
                pageTitle = request.pageTitle,
                content = request.content,
                timestamp = request.timestamp.withOffsetSameInstant(ZoneOffset.UTC)
            )
    }
}
