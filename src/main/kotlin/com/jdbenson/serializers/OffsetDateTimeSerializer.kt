package com.jdbenson.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class OffsetDateTimeSerializer : JsonSerializer<OffsetDateTime>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(value: OffsetDateTime, gen: JsonGenerator, arg2: SerializerProvider) {
        val x = value.toString()
        gen.writeString(x)
    }
}
