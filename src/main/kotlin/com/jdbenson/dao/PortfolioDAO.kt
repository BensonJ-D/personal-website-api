package com.jdbenson.dao

import com.jdbenson.domain.documents.AboutDocument
import com.jdbenson.domain.documents.ContentDocument
import com.mongodb.client.result.InsertOneResult

interface PortfolioDAO {
    suspend fun getNewestAboutContent(): AboutDocument?
    suspend fun updateAboutContent(about: AboutDocument): InsertOneResult

    suspend fun getNewestPageContent(page: String): ContentDocument?
    suspend fun updatePageContent(content: ContentDocument): InsertOneResult
}