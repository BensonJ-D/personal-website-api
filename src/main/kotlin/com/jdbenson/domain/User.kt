package com.jdbenson.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.litote.kmongo.Id

data class User(
    @JsonSerialize(using = ToStringSerializer::class)
    val _id: Id<String>?,
    val name: String,
    val age: Int)