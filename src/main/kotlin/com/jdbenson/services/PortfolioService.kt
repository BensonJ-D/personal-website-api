package com.jdbenson.services

import com.google.inject.Inject
import com.jdbenson.dao.PortfolioDAO
import com.jdbenson.domain.documents.ContentDocument
import com.mongodb.client.result.InsertOneResult

class PortfolioService @Inject constructor(private val portfolioDAO: PortfolioDAO,
                                           private val webSocketService: WebSocketService) {
    suspend fun getNewestPageContent(page: String) = portfolioDAO.getNewestPageContent(page)
    suspend fun updatePageContent(content: ContentDocument): InsertOneResult {
        val updatedContentResult = portfolioDAO.updatePageContent(content)
        if(updatedContentResult.wasAcknowledged()) {
            webSocketService.sendAll(content.pageTitle)
        }

        return updatedContentResult
    }
}