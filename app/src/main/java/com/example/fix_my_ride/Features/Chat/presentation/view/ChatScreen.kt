package com.example.fix_my_ride.Features.Chat.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fix_my_ride.Features.Chat.presentation.viewmodel.ChatViewModel
import com.example.fix_my_ride.Features.Chat.domain.model.Message
import com.example.fix_my_ride.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen(
    vendorId   : String,
    vendorName : String,
    onBack     : () -> Unit,
    viewModel  : ChatViewModel = hiltViewModel()
) {
    val uiState  by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope     = rememberCoroutineScope()

    // ✅ FIX 1: sirf vendorId — userId ViewModel se Firebase se milta hai
    LaunchedEffect(vendorId) {
        viewModel.initChat(vendorId)
    }

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(uiState.messages.lastIndex)
            }
        }
    }

    Scaffold(
        containerColor = DashBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            ChatTopBar(
                vendorName = vendorName,
                onBack     = onBack
            )

            Box(modifier = Modifier.weight(1f)) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            color    = Primary,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    uiState.messages.isEmpty() -> {
                        EmptyChatState(
                            vendorName = vendorName,
                            modifier   = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(
                            state          = listState,
                            modifier       = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                horizontal = 16.dp,
                                vertical   = 12.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = uiState.messages,
                                key   = { it.messageId }
                            ) { message ->
                                MessageBubble(
                                    message       = message,
                                    // ✅ FIX 2: uiState se milta hai — Firebase se auto
                                    currentUserId = uiState.currentUserId
                                )
                            }
                        }
                    }
                }
            }

            uiState.error?.let { error ->
                Text(
                    text     = error,
                    color    = Color.Red.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red.copy(alpha = 0.08f))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            ChatInputBar(
                value         = uiState.inputText,
                onValueChange = viewModel::onInputChange,
                onSend        = {
                    // ✅ FIX 3: sirf vendorId — senderId ViewModel handle karta hai
                    viewModel.sendMessage(vendorId = vendorId)
                }
            )
        }
    }
}

// ── Top Bar ───────────────────────────────────────────
@Composable
private fun ChatTopBar(
    vendorName : String,
    onBack     : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DashCardBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(DashBackground),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector        = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint               = DashTextPrimary,
                    modifier           = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = vendorName.take(2).uppercase(),
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize   = 14.sp,
                color      = Primary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text       = vendorName,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize   = 15.sp,
                color      = DashTextPrimary
            )
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22C55E))
                )
                Text(
                    text       = "Online",
                    fontFamily = Roboto,
                    fontSize   = 11.sp,
                    color      = Color(0xFF22C55E)
                )
            }
        }
    }
}

// ── Message Bubble ────────────────────────────────────
@Composable
private fun MessageBubble(
    message       : Message,
    currentUserId : String
) {
    val isMine = message.senderId == currentUserId
    val time   = remember(message.timestamp) {
        SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Date(message.timestamp))
    }

    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
        verticalAlignment     = Alignment.Bottom
    ) {
        if (!isMine) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "V",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 11.sp,
                    color      = Primary
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        Column(
            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart    = 18.dp,
                            topEnd      = 18.dp,
                            bottomStart = if (isMine) 18.dp else 4.dp,
                            bottomEnd   = if (isMine) 4.dp else 18.dp
                        )
                    )
                    .background(if (isMine) Primary else DashCardBg)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .widthIn(max = 260.dp)
            ) {
                Text(
                    text       = message.text,
                    fontFamily = Roboto,
                    fontSize   = 13.sp,
                    color      = if (isMine) Color.White else DashTextPrimary,
                    lineHeight = 19.sp
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text       = time,
                    fontFamily = Roboto,
                    fontSize   = 10.sp,
                    color      = DashTextLight
                )
                if (isMine) {
                    Text(
                        text  = if (message.isRead) "✓✓" else "✓",
                        fontSize = 10.sp,
                        color = if (message.isRead) Primary else DashTextLight
                    )
                }
            }
        }
    }
}

// ── Input Bar ─────────────────────────────────────────
@Composable
private fun ChatInputBar(
    value         : String,
    onValueChange : (String) -> Unit,
    onSend        : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DashCardBg)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            modifier      = Modifier.weight(1f),
            placeholder   = {
                Text(
                    text       = "Type a message...",
                    fontFamily = Roboto,
                    fontSize   = 13.sp,
                    color      = DashTextLight
                )
            },
            shape  = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = Primary,
                unfocusedBorderColor = DashTextLight.copy(alpha = 0.3f),
                focusedTextColor     = DashTextPrimary,
                unfocusedTextColor   = DashTextPrimary,
                cursorColor          = Primary
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSend() }),
            singleLine      = true
        )

        Spacer(modifier = Modifier.width(10.dp))

        val canSend = value.isNotBlank()
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(if (canSend) Primary else Primary.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onSend, enabled = canSend) {
                Icon(
                    imageVector        = Icons.Default.Send,
                    contentDescription = "Send",
                    tint               = Color.White,
                    modifier           = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ── Empty State ───────────────────────────────────────
@Composable
private fun EmptyChatState(
    vendorName : String,
    modifier   : Modifier = Modifier
) {
    Column(
        modifier            = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "💬", fontSize = 28.sp)
        }
        Text(
            text       = "Start a conversation",
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize   = 16.sp,
            color      = DashTextPrimary
        )
        Text(
            text       = "Say hello to $vendorName!",
            fontFamily = Roboto,
            fontSize   = 13.sp,
            color      = DashTextSecondary
        )
    }
}