package org.takeyourdata.service.server.databases

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.takeyourdata.service.server.ConfigProperties
import org.takeyourdata.service.server.databases.structure.Messages
import java.util.*

class PostgresqlClient {
    val properties: Properties = ConfigProperties().get()

    fun connect() {
        val address: String = properties.getProperty("database.postgresql.address")

        Database.connect(
            "$address?createDatabaseIfNotExist=true",
            "org.postgresql.Driver",
            properties.getProperty("database.postgresql.username"),
            properties.getProperty("database.postgresql.password")
        )

        transaction {
            SchemaUtils.create(
                Messages
            )
        }
    }
}