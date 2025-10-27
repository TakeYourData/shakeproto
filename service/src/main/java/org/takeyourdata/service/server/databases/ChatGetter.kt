package org.takeyourdata.service.server.databases

import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.takeyourdata.service.server.databases.structure.Message

class ChatGetter(
    val chatId: Int
) {
    fun getChat() {
        transaction {
            val header = Message[chatId]
            header.storeWrittenValues()
        }

        return
    }
}