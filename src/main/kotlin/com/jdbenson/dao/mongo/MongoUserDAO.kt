package com.jdbenson.dao.mongo

import com.google.inject.Inject
import com.google.inject.name.Named
import com.jdbenson.dao.UserDAO
import com.jdbenson.domain.User
import com.mongodb.client.result.InsertOneResult
import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoUserDAO @Inject constructor(@Named("UserDatabase") val userDatabase: CoroutineDatabase): UserDAO {
    override suspend fun find(id: String?): User? = userDatabase.getCollection<User>("users").findOne("{_id: '$id'}")
    override suspend fun findAll(): List<User> = userDatabase.getCollection<User>("users").find().toList()
    override suspend fun insert(user: User): InsertOneResult = userDatabase.getCollection<User>("users").insertOne(user)
    override suspend fun delete(id: String?): Boolean = userDatabase.getCollection<User>("users").deleteOne("{_id: '$id'}").wasAcknowledged()
}