package com.jdbenson.resources
import com.google.inject.Inject
import com.jdbenson.dao.PortfolioDAO
import com.jdbenson.domain.api.request.AboutRequest
import com.jdbenson.domain.api.response.AboutResponse
import com.jdbenson.domain.documents.AboutDocument
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.request.*

class AboutResource @Inject constructor(application: Application,
                                        portfolioDAO: PortfolioDAO
) {
    init {
        application.routing {
            route("/about") {
                get {
                    try {
                        val aboutDocument = portfolioDAO.getNewestAboutContent() ?: throw NotFoundException("Content not found")
                        val responseBody = aboutDocument.toResponse()
                        call.respond(responseBody)
                    } catch (e: Exception) {
                        application.log.error(e.localizedMessage)
                    }
                }

                post {
                    call.parameters
                    val requestBody = call.receive<AboutRequest>()
                    val aboutDocument = AboutDocument.fromRequest(requestBody)
                    val insertResult = portfolioDAO.updateAboutContent(aboutDocument)
                    val isSuccess = insertResult.wasAcknowledged()

                    if(isSuccess)
                        return@post call.respond(requestBody)
                }
            }
        }
    }
}

