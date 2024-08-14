package com.jdbenson.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException
import java.time.LocalDateTime
import java.time.OffsetDateTime

class OffsetDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): LocalDateTime  {
        return LocalDateTime.parse(p0?.text)
    }
}