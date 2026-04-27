package com.example.fix_my_ride.Features.Chat.data.model

import com.example.fix_my_ride.Features.Chat.domain.model.Chat

data class ChatDto(
    val chatId: String = "",
    val userId: String = "",
    val vendorId: String = "",
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L,
    val unreadCount: Int = 0
) {
    fun toDomain() = Chat(
        chatId = chatId,
        userId = userId,
        vendorId = vendorId,
        lastMessage = lastMessage,
        lastMessageTime = lastMessageTime,
        unreadCount = unreadCount
    )
}