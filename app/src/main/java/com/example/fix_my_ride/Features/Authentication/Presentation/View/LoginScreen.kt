package com.example.fix_my_ride.Features.Authentication.Presentation.View

// features/auth/presentation/LoginScreen.kt
// features/auth/presentation/LoginScreen.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fix_my_ride.R
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Authentication.Presentation.ViewModel.AuthViewModel
import com.example.fix_my_ride.ui.Components.AppButton
import com.example.fix_my_ride.ui.Components.AppTextField
import com.example.fix_my_ride.ui.Components.BackButton
import com.example.fix_my_ride.ui.Components.ErrorBanner
import com.example.fix_my_ride.ui.theme.Background
import com.example.fix_my_ride.ui.theme.CardBg
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.PrimaryDark
import com.example.fix_my_ride.ui.theme.Roboto
import com.example.fix_my_ride.ui.theme.TextPrimary
import com.example.fix_my_ride.ui.theme.TextSecondary
@Composable
fun LoginScreen(
    onLoginSuccess       :  () -> Unit,
    onNavigateToRegister : () -> Unit,
    onBack               : () -> Unit = {},
    viewModel            : AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val loginState by viewModel.loginState
        .collectAsStateWithLifecycle()
    val state by viewModel.loginState.collectAsStateWithLifecycle()

    val isLoading = loginState is AuthResult.Loading
    val errorMessage = (loginState as? AuthResult.Error)?.message

    LaunchedEffect(loginState) {
        if (loginState is AuthResult.Success) {
            viewModel.resetLoginState()
            onLoginSuccess()
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally  // ✅ Yeh add karo
        ) {
            Spacer(modifier = Modifier.height(52.dp))

            // ── Back Button — Left align rakhna hai ────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                BackButton(onClick = onBack)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Top Icon — Ab center mein aayega ──────────
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = Primary.copy(alpha = 0.2f),
                        spotColor = Primary.copy(alpha = 0.3f)
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Primary, PrimaryDark)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Header — Center align ──────────────────────
            Text(
                text = stringResource(R.string.login_title),
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = TextPrimary,
                textAlign = TextAlign.Center       // ✅ Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.login_subtitle),
                fontFamily = Roboto,
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center       // ✅ Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── White Card ─────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardBg
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    ErrorBanner(message = errorMessage)

                    AppTextField(
                        value = email,
                        onValueChange = { email = it.trim() },
                        label = stringResource(R.string.label_email),
                        placeholder = stringResource(R.string.placeholder_email),
                        leadingIcon = Icons.Default.Email,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                    )

                    AppTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = stringResource(R.string.label_password),
                        leadingIcon = Icons.Default.Lock,
                        trailingIcon = if (passwordVisible)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                        onTrailingIconClick = { passwordVisible = !passwordVisible },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                viewModel.loginUser(email, password)
                            }
                        )
                    )

                    // ── Forgot Password ───────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { /* TODO */ }) {
                            Text(
                                text = "Forgot Password?",
                                fontFamily = Roboto,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = Primary
                            )
                        }
                    }

                    AppButton(
                        text = stringResource(R.string.btn_login),
                        loadingText = stringResource(R.string.btn_logging_in),
                        onClick = {
                            keyboardController?.hide()
                            viewModel.loginUser(email, password)
                        },
                        isLoading = isLoading
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Register Link ──────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.no_account),
                    fontFamily = Roboto,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        text = stringResource(R.string.go_to_register),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))


        }
    }
}