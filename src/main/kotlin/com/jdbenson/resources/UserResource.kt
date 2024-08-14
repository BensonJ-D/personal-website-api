package com.jdbenson.resources
import com.google.inject.Inject
import com.jdbenson.dao.UserDAO
import com.jdbenson.domain.User
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

class UserResource @Inject constructor(application: Application,
                                       userDAO: UserDAO
) {
    init {
        application.routing {
            get("/users") {
                val users = userDAO.findAll()
                call.respond(users)
            }
            post("/user") {
                call.parameters
                val requestBody = call.receive<User>()
                val insertResult = userDAO.insert(requestBody)
                val isSuccess = insertResult.wasAcknowledged()
                call.respond(isSuccess)
            }

            get("/user") {
                val id = call.request.queryParameters["id"]
                val user = userDAO.find(id)
                val response = user ?: "User not found"
                call.respond(response)
            }

            delete("/user") {
                val id = call.request.queryParameters["id"]
                val isSuccess = userDAO.delete(id)
                call.respond(isSuccess)
            }
        }
    }
}

