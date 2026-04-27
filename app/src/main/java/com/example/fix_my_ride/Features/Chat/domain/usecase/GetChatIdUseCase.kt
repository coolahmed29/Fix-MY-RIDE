package com.example.fix_my_ride.Features.Chat.domain.usecase

import com.example.fix_my_ride.Features.Chat.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatIdUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(userId: String, vendorId: String): Result<String> {
        return repository.getOrCreateChatId(userId, vendorId)
    }
}