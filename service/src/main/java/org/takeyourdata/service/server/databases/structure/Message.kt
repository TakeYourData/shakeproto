package org.takeyourdata.service.server.databases.structure

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class Message(chatId: EntityID<Int>) : IntEntity(chatId) {
    var msgId by Messages.msgId
    var senderId by Messages.senderId
    var recipientId by Messages.recipientId
    var timestamp by Messages.timestamp
    var content by Messages.content

    companion object : IntEntityClass<Message>(Messages) {
        fun isExist(msgId: Int): Boolean {
            return transaction {
                Message.findById(msgId) != null
            }
        }

        fun create(
            chatId: Int,
            msgId: Int,
            senderId: Int,
            recipientId: Int,
            timestamp: Long,
            content: ByteArray
        ) {

            transaction {
                Message.new(chatId) {
                    this.msgId      = msgId
                    this.senderId    = senderId
                    this.recipientId = recipientId
                    this.timestamp   = timestamp
                    this.content     = content
                }
            }
        }
    }
}