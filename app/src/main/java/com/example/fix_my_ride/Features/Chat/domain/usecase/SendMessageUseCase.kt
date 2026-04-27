package com.example.fix_my_ride.Features.Chat.domain.usecase

import com.example.fix_my_ride.Features.Chat.domain.model.Message
import com.example.fix_my_ride.Features.Chat.domain.model.SenderType
import com.example.fix_my_ride.Features.Chat.domain.repository.ChatRepository
import java.util.UUID
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        chatId: String,
        senderId: String,
        receiverId: String,
        text: String,
        senderType: SenderType
    ): Result<Unit> {
        if (text.isBlank()) return Result.failure(Exception("Message cannot be empty"))

        val message = Message(
            messageId = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = senderId,
            receiverId = receiverId,
            text = text.trim(),
            timestamp = System.currentTimeMillis(),
            senderType = senderType
        )
        return repository.sendMessage(message)
    }
}