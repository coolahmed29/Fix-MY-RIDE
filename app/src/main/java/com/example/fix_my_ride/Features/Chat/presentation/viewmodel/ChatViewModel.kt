package com.example.fix_my_ride.Features.Chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fix_my_ride.Features.Chat.domain.model.Message
import com.example.fix_my_ride.Features.Chat.domain.model.SenderType
import com.example.fix_my_ride.Features.Chat.domain.usecase.GetChatIdUseCase
import com.example.fix_my_ride.Features.Chat.domain.usecase.ListenMessagesUseCase
import com.example.fix_my_ride.Features.Chat.domain.usecase.SendMessageUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages  : List<Message> = emptyList(),
    val inputText : String        = "",
    val isLoading : Boolean       = false,
    val error     : String?       = null,
    val chatId    : String?       = null,
    val currentUserId : String    = ""  // ✅ Firebase se auto fill
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatIdUseCase     : GetChatIdUseCase,
    private val sendMessageUseCase   : SendMessageUseCase,
    private val listenMessagesUseCase: ListenMessagesUseCase,
    private val firebaseAuth         : FirebaseAuth          // ✅ Direct inject
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    // ✅ currentUserId Firebase se lo — manually pass karne ki zaroorat nahi
    private val currentUserId: String
        get() = firebaseAuth.currentUser?.uid ?: ""

    fun initChat(vendorId: String) {
        val userId = currentUserId

        if (userId.isEmpty()) {
            _uiState.update { it.copy(error = "User logged in nahi hai") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null, currentUserId = userId) }

        viewModelScope.launch {
            getChatIdUseCase(userId, vendorId)
                .onSuccess { chatId ->
                    _uiState.update { it.copy(chatId = chatId, isLoading = false) }
                    listenMessages(chatId)
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error     = e.message ?: "Chat shuru karne mein masla hua"
                        )
                    }
                }
        }
    }

    private fun listenMessages(chatId: String) {
        viewModelScope.launch {
            listenMessagesUseCase(chatId)
                .catch { e -> _uiState.update { it.copy(error = e.message) } }
                .collect { messages -> _uiState.update { it.copy(messages = messages) } }
        }
    }

    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text, error = null) }
    }

    fun sendMessage(vendorId: String) {
        val chatId = _uiState.value.chatId ?: return
        val text   = _uiState.value.inputText.trim()
        val userId = currentUserId

        if (text.isBlank() || userId.isEmpty()) return

        _uiState.update { it.copy(inputText = "") }

        viewModelScope.launch {
            sendMessageUseCase(
                chatId     = chatId,
                senderId   = userId,
                receiverId = vendorId,
                text       = text,
                senderType = SenderType.USER
            ).onFailure { e ->
                _uiState.update {
                    it.copy(
                        error     = e.message ?: "Message send nahi hua",
                        inputText = text
                    )
                }
            }
        }
    }
}