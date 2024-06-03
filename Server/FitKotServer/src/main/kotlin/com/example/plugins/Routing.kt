package com.example.plugins

import com.example.plugins.Users.email
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureRouting() {
    routing {
        get("/users") {
            val users = transaction {
                Users.selectAll().map {
                    User(
                        id = it[Users.id],
                        email = it[Users.email],
                        fullName = it[Users.fullName],
                        password = it[Users.password],
                        role = it[Users.role]
                    )
                }
            }
            call.respond(users)
        }

        post("/users") {
            val user = call.receive<User>()
            transaction {
                Users.insert {
                    it[email] = user.email
                    it[fullName] = user.fullName
                    it[password] = user.password
                    it[role] = user.role
                }
            }
            call.respond(HttpStatusCode.Created)
        }

        get("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val user = transaction {
                Users.select { Users.id eq id }.singleOrNull()?.let {
                    User(
                        id = it[Users.id],
                        email = it[Users.email],
                        fullName = it[Users.fullName],
                        password = it[Users.password],
                        role = it[Users.role]
                    )
                }
            }
            if (user != null) {
                call.respond(user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        put("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
            val updatedUser = call.receive<User>()
            transaction {
                Users.update({ Users.id eq id }) {
                    it[email] = updatedUser.email
                    it[fullName] = updatedUser.fullName
                    it[password] = updatedUser.password
                    it[role] = updatedUser.role
                }
            }
            call.respond(HttpStatusCode.OK)
        }

        delete("/users/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            transaction {
                Users.deleteWhere { Users.id eq id }
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}
