package com.jdbenson.plugins

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.name.Names
import com.jdbenson.MongoDatabase
import com.jdbenson.dao.PortfolioDAO
import com.jdbenson.dao.UserDAO
import com.jdbenson.dao.mongo.MongoPortfolioDAO
import com.jdbenson.dao.mongo.MongoUserDAO
import com.jdbenson.services.WebSocketService
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.litote.kmongo.coroutine.CoroutineDatabase

fun Application.configureDependencyInjection() {
    Guice.createInjector(AppModule(this))
}

class AppModule(private val application: Application) : AbstractModule() {
    override fun configure() {
        application.bindDatabases()
        application.bindResources()

        bind(Application::class.java).toInstance(application)
        bind(UserDAO::class.java).to(MongoUserDAO::class.java)
        bind(PortfolioDAO::class.java).to(MongoPortfolioDAO::class.java)
        bind(WebSocketService::class.java).asEagerSingleton()
    }

    private fun Application.bindResources() {
        environment.config.tryGetStringList("ktor.application.resources")?.forEach {
                val javaClass = Class.forName(it)
                bind(javaClass).asEagerSingleton()
            }
    }

    private fun Application.bindDatabases() {
        environment.config.configList("ktor.application.databases.mongo")
            .forEach {
                val mongoConfiguration = object {
                    val annotationName = it.tryGetString("annotationName")
                    val connectionString = it.tryGetString("connectionString")
                    val databaseName = it.tryGetString("databaseName")
                }

                if(mongoConfiguration.annotationName == null || mongoConfiguration.connectionString == null || mongoConfiguration.databaseName == null) return@forEach

                val database = MongoDatabase(
                    connectionString = mongoConfiguration.connectionString,
                    database = mongoConfiguration.databaseName
                ).connect()

                bind(CoroutineDatabase::class.java).annotatedWith(Names.named(mongoConfiguration.annotationName)).toInstance(database)
            }
    }
}

