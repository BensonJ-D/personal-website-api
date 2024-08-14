package com.jdbenson.resources
import com.google.inject.Inject
import com.jdbenson.dao.PortfolioDAO
import com.jdbenson.domain.api.request.ContentNewRequest
import com.jdbenson.domain.api.request.ContentUpdateRequest
import com.jdbenson.domain.documents.ContentDocument
import com.jdbenson.services.PortfolioService
import com.jdbenson.services.WebSocketService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.request.*

class PageResource @Inject constructor(application: Application,
                                       portfolioService: PortfolioService,
                                       webSocketService: WebSocketService
) {
    init {
        application.routing {
            route("/pages") {
                get("/get-content/{page}") {
                    try {
                        val page = call.parameters["page"] ?: throw NotFoundException("Page not found")
                        val contentDocument = portfolioService.getNewestPageContent(page)
                            ?: throw NotFoundException("Content not found")
                        val responseBody = contentDocument.toResponse()
                        call.respond(responseBody)
                    } catch (e: Exception) {
                        application.log.error(e.localizedMessage)
                    }
                }

                authenticate("post-auth") {
                    post {
                        call.parameters
                        val requestBody = call.receive<ContentUpdateRequest>()
                        val newRoute = requestBody.route

                        portfolioService.getNewestPageContent(newRoute)
                            ?: throw NotFoundException("Content not found")

                        val contentDocument = ContentDocument.fromRequest(requestBody)
                        val insertResult = portfolioService.updatePageContent(contentDocument)
                        val isSuccess = insertResult.wasAcknowledged()

                        if (isSuccess)
                            return@post call.respond(requestBody)
                    }

                    post("/new-page") {
                        call.parameters
                        val requestBody = call.receive<ContentNewRequest>()
                        val newRoute = requestBody.route

                        val existingDocument = portfolioService.getNewestPageContent(newRoute)

                        if(existingDocument != null) throw BadRequestException("Page already exists")

                        val contentDocument = ContentDocument.fromRequest(requestBody)
                        val insertResult = portfolioService.updatePageContent(contentDocument)
                        val isSuccess = insertResult.wasAcknowledged()

                        if (isSuccess)
                            return@post call.respond(requestBody)
                    }
                }

                post("/send") {
                    call.parameters
                    val requestBody = call.receive<ContentUpdateRequest>()
                    webSocketService.sendAll(requestBody)

                    return@post call.respond(requestBody)
                }
            }
        }
    }
}

