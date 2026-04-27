package com.example.fix_my_ride.Features.Chat.data.repository

import com.example.fix_my_ride.Features.Chat.data.source.ChatFirebaseSource
import com.example.fix_my_ride.Features.Chat.domain.model.Message
import com.example.fix_my_ride.Features.Chat.domain.model.Chat   // ✅ add
import com.example.fix_my_ride.Features.Chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow   // ✅ add
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firebaseSource: ChatFirebaseSource
) : ChatRepository {

    override suspend fun getOrCreateChatId(userId: String, vendorId: String) =
        firebaseSource.getOrCreateChat(userId, vendorId)

    override suspend fun sendMessage(message: Message) =
        firebaseSource.sendMessage(message)

    override fun listenToMessages(chatId: String): Flow<List<Message>> =
        firebaseSource.listenToMessages(chatId)

    override fun listenToChats(userId: String): Flow<List<Chat>> {
        // Implement if chat list screen chahiye
        TODO()
    }

    override suspend fun markMessagesAsRead(chatId: String, currentUserId: String) =
        firebaseSource.markAsRead(chatId, currentUserId)
}