package com.example.fix_my_ride.Features.Authentication.Presentation.View

// features/auth/presentation/LoginScreen.kt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusDirection
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fix_my_ride.R
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Authentication.Presentation.ViewModel.AuthViewModel
import com.example.fix_my_ride.ui.Components.AppButton
import com.example.fix_my_ride.ui.Components.AppTextField
import com.example.fix_my_ride.ui.Components.ErrorBanner

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    // ── Local UI State ─────────────────────────────────
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // ── ViewModel State ────────────────────────────────
    val loginState by viewModel.loginState
        .collectAsStateWithLifecycle()

    val isLoading    = loginState is AuthResult.Loading
    val errorMessage = (loginState as? AuthResult.Error)?.message

    // ── Navigation ─────────────────────────────────────
    LaunchedEffect(loginState) {
        if (loginState is AuthResult.Success) {
            viewModel.resetLoginState()
            onLoginSuccess()
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager       = LocalFocusManager.current

    // ── UI ─────────────────────────────────────────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // ── Header ─────────────────────────────────────
        Text(
            text       = stringResource(R.string.login_title),
            style      = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground,
            textAlign  = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text      = stringResource(R.string.login_subtitle),
            style     = MaterialTheme.typography.bodyMedium,
            color     = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // ── Error Banner ───────────────────────────────
        ErrorBanner(message = errorMessage)

        // ── Email Field ────────────────────────────────
        AppTextField(
            value           = email,
            onValueChange   = { email = it.trim() },
            label           = stringResource(R.string.label_email),
            placeholder     = stringResource(R.string.placeholder_email),
            leadingIcon     = Icons.Default.Email,
            modifier        = Modifier.fillMaxWidth(),
            enabled         = !isLoading,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction    = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ── Password Field ─────────────────────────────
        AppTextField(
            value           = password,
            onValueChange   = { password = it },
            label           = stringResource(R.string.label_password),
            leadingIcon     = Icons.Default.Lock,
            trailingIcon    = if (passwordVisible)
                Icons.Default.VisibilityOff
            else
                Icons.Default.Visibility,
            onTrailingIconClick  = { passwordVisible = !passwordVisible },
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            modifier        = Modifier.fillMaxWidth(),
            enabled         = !isLoading,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction    = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    viewModel.loginUser(email, password)
                }
            )
        )

        Spacer(modifier = Modifier.height(28.dp))

        // ── Login Button ───────────────────────────────
        AppButton(
            text        = stringResource(R.string.btn_login),
            loadingText = stringResource(R.string.btn_logging_in),
            onClick     = {
                keyboardController?.hide()
                viewModel.loginUser(email, password)
            },
            isLoading = isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Register Link ──────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text  = stringResource(R.string.no_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(
                onClick = onNavigateToRegister,
                enabled = !isLoading
            ) {
                Text(
                    text  = stringResource(R.string.go_to_register),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}