package com.example.fix_my_ride.Features.Chat.data.source

import com.example.fix_my_ride.Features.Chat.data.model.ChatDto
import com.example.fix_my_ride.Features.Chat.data.model.MessageDto
import com.example.fix_my_ride.Features.Chat.data.model.toDto
import com.example.fix_my_ride.Features.Chat.domain.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.collections.mapNotNull

class ChatFirebaseSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun generateChatId(userId: String, vendorId: String): String {
        return listOf(userId, vendorId).sorted().joinToString("_")
    }

    suspend fun getOrCreateChat(userId: String, vendorId: String): Result<String> {
        val chatId = generateChatId(userId, vendorId)
        return try {
            val ref = firestore.collection("chats").document(chatId)
            val doc = ref.get().await()
            if (!doc.exists()) {
                val chatDto = ChatDto(
                    chatId = chatId,
                    userId = userId,
                    vendorId = vendorId
                )
                ref.set(chatDto).await()
            }
            Result.success(chatId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMessage(message: Message): Result<Unit> {
        return try {
            val dto = message.toDto()

            firestore.collection("chats")
                .document(message.chatId)
                .collection("messages")
                .document(message.messageId)
                .set(dto)
                .await()

            // ✅ FIX: Explicit Map<String, Any> type
            val updates: Map<String, Any> = mapOf(
                "lastMessage" to message.text,
                "lastMessageTime" to message.timestamp
            )

            firestore.collection("chats")
                .document(message.chatId)
                .update(updates)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun listenToMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents
                    ?.mapNotNull { doc ->
                        doc.toObject(MessageDto::class.java)?.toDomain()
                    }
                    ?: emptyList()
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }

    suspend fun markAsRead(chatId: String, currentUserId: String): Result<Unit> {
        return try {
            val unreadMessages = firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .whereEqualTo("receiverId", currentUserId)
                .whereEqualTo("isRead", false)
                .get()
                .await()

            val batch = firestore.batch()
            unreadMessages.documents.forEach {
                batch.update(it.reference, "isRead", true)
            }
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}