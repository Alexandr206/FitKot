package com.example.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table() {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 255)
    val fullName = varchar("fullName", 255)
    val password = varchar("password", 255)
    val role = varchar("role", 255)
}

fun Application.configureDatabase() {
    val database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/fitkot_db",
        driver = "org.postgresql.Driver",
        user = "fitkot_user",
        password = "password"
    )
    transaction(database) {
        SchemaUtils.create(Users)
    }
}

data class User(
    val id: Int,
    val email: String,
    val fullName: String,
    val password: String,
    val role: String
)
