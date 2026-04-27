package com.example.fix_my_ride.Features.Chat.data.model

import com.example.fix_my_ride.Features.Chat.domain.model.Message
import com.example.fix_my_ride.Features.Chat.domain.model.SenderType

data class MessageDto(
    val messageId: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val isRead: Boolean = false,
    val senderType: String = "USER"
) {
    fun toDomain() = Message(
        messageId = messageId,
        chatId = chatId,
        senderId = senderId,
        receiverId = receiverId,
        text = text,
        timestamp = timestamp,
        isRead = isRead,
        senderType = SenderType.valueOf(senderType)
    )
}

fun Message.toDto() = MessageDto(
    messageId = messageId,
    chatId = chatId,
    senderId = senderId,
    receiverId = receiverId,
    text = text,
    timestamp = timestamp,
    isRead = isRead,
    senderType = senderType.name
)