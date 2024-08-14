package com.jdbenson.dao

import com.jdbenson.domain.User
import com.mongodb.client.result.InsertOneResult

interface UserDAO {
    suspend fun find(id: String?): User?
    suspend fun findAll(): List<User>
    suspend fun insert(user: User): InsertOneResult
    suspend fun delete(id: String?): Boolean
}