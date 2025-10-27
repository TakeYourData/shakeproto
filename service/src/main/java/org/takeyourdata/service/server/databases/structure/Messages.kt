package org.takeyourdata.service.server.databases.structure

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object Messages : IntIdTable("messages") {
    val msgId            = integer("msgId")
    val senderId         = integer("senderId")
    val recipientId      = integer("recipientId")
    val timestamp        = long("timestamp")
    val content          = binary("content")
}
