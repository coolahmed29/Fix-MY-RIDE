package com.example.fix_my_ride.Features.Chat.domain.model

data class Message(
    val messageId: String,
    val chatId: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val senderType: SenderType // USER ya VENDOR
)

enum class SenderType { USER, VENDOR }