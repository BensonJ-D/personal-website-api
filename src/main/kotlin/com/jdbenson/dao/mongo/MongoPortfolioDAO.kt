package com.jdbenson.dao.mongo

import com.google.inject.Inject
import com.google.inject.name.Named
import com.jdbenson.dao.PortfolioDAO
import com.jdbenson.domain.documents.AboutDocument
import com.jdbenson.domain.documents.ContentDocument
import com.mongodb.client.result.InsertOneResult
import org.bson.BsonDocument
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoPortfolioDAO @Inject constructor(@Named("PortfolioDatabase") val portfolioDatabase: CoroutineDatabase): PortfolioDAO {
    private val aboutIndexHint = BsonDocument.parse("{ version: -1, timestamp: -1 }")
    private val contentIndexHint = BsonDocument.parse("{ route: 1, version: -1, timestamp: -1 }")

    override suspend fun getNewestAboutContent(): AboutDocument? =
        portfolioDatabase.getCollection<AboutDocument>("about")
            .find(AboutDocument::version eq 1).hint(aboutIndexHint).first()

    override suspend fun updateAboutContent(about: AboutDocument): InsertOneResult =
        portfolioDatabase.getCollection<AboutDocument>("about")
            .insertOne(about)

    override suspend fun getNewestPageContent(page: String): ContentDocument? =
        portfolioDatabase.getCollection<ContentDocument>("pages")
            .find(ContentDocument::route eq page, ContentDocument::version eq 1)
            .hint(contentIndexHint).first()

    override suspend fun updatePageContent(content: ContentDocument): InsertOneResult =
        portfolioDatabase.getCollection<ContentDocument>("pages")
            .insertOne(content)
}