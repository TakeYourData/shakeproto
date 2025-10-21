package org.takeyourdata.service.server.databases.structure

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class Message(msgId: EntityID<Int>) : IntEntity(msgId) {
    var chatId by Messages.chatId
    var senderId by Messages.senderId
    var recipientId by Messages.recipientId
    var authId by Messages.authId
    var timestamp by Messages.timestamp
    var content by Messages.content

    companion object : IntEntityClass<Message>(Messages) {
        fun isExist(msgId: Int): Boolean {
            return transaction {
                Message.findById(msgId) != null
            }
        }

        fun create(
            msgId: Int,
            chatId: Int,
            senderId: Int,
            recipientId: Int,
            authId: ByteArray,
            timestamp: Long,
            content: ByteArray
        ) {

            transaction {
                Message.new(msgId) {
                    this.chatId      = chatId
                    this.senderId    = senderId
                    this.recipientId = recipientId
                    this.authId      = authId
                    this.timestamp   = timestamp
                    this.content     = content
                }
            }
        }
    }
}