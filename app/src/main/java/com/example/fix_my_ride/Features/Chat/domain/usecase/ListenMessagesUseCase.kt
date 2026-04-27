package com.example.fix_my_ride.Features.Chat.domain.usecase

import com.example.fix_my_ride.Features.Chat.domain.model.Message
import com.example.fix_my_ride.Features.Chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<List<Message>> {
        return repository.listenToMessages(chatId)
    }
}