package com.jdbenson

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.util.KMongoConfiguration

class MongoDatabase constructor(var connectionString: String, var database: String) {
    fun connect(): CoroutineDatabase {
        KMongoConfiguration.resetConfiguration()
        val client = KMongo.createClient(connectionString)
        val databaseConnection = client.getDatabase(database)
        return databaseConnection.coroutine
    }
}
