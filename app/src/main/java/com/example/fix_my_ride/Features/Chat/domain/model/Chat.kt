package com.example.fix_my_ride.Features.Chat.domain.model

data class Chat(
    val chatId: String,
    val userId: String,
    val vendorId: String,
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L,
    val unreadCount: Int = 0
)