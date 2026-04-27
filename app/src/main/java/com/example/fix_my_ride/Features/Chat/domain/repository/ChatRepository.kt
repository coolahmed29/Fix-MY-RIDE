package com.example.fix_my_ride.Features.Chat.domain.repository

import com.example.fix_my_ride.Features.Chat.domain.model.Chat
import com.example.fix_my_ride.Features.Chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getOrCreateChatId(userId: String, vendorId: String): Result<String>
    suspend fun sendMessage(message: Message): Result<Unit>
    fun listenToMessages(chatId: String): Flow<List<Message>>
    fun listenToChats(userId: String): Flow<List<Chat>>
    suspend fun markMessagesAsRead(chatId: String, currentUserId: String): Result<Unit>
}