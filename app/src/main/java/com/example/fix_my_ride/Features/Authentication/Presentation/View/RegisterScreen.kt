package com.example.fix_my_ride.Features.Authentication.Presentation.View

// features/auth/presentation/RegisterScreen.kt

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.text.input.KeyboardCapitalization
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
import com.example.fix_my_ride.ui.theme.Montserrat

@Composable
fun RegisterScreen(
    onRegistrationSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    // ── Local UI State ─────────────────────────────────
    var name            by remember { mutableStateOf("") }
    var phone           by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // ── ViewModel State ────────────────────────────────
    val registerState by viewModel.registerState
        .collectAsStateWithLifecycle()

    val isLoading    = registerState is AuthResult.Loading
    val errorMessage = (registerState as? AuthResult.Error)?.message

    // ── Navigation ─────────────────────────────────────
    LaunchedEffect(registerState) {
        if (registerState is AuthResult.Success) {
            viewModel.resetRegisterState()
            onRegistrationSuccess()
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
        Spacer(modifier = Modifier.height(60.dp))

        // ── Header ─────────────────────────────────────
        Text(
            text       = stringResource(R.string.register_title),
            style      = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground,
            textAlign  = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text      = stringResource(R.string.register_subtitle),
            style     = MaterialTheme.typography.bodyMedium,
            color     = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(36.dp))

        // ── Error Banner ───────────────────────────────
        ErrorBanner(message = errorMessage)

        // ── Name Field ─────────────────────────────────
        AppTextField(
            value         = name,
            onValueChange = { name = it },
            label         = stringResource(R.string.label_name),
            placeholder   = stringResource(R.string.placeholder_name),
            leadingIcon   = Icons.Default.Person,
            modifier      = Modifier.fillMaxWidth(),
            enabled       = !isLoading,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction      = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ── Phone Field ────────────────────────────────
        AppTextField(
            value         = phone,
            onValueChange = {
                if (it.all { c -> c.isDigit() } && it.length <= 11)
                    phone = it
            },
            label         = stringResource(R.string.label_phone),
            placeholder   = stringResource(R.string.placeholder_phone),
            leadingIcon   = Icons.Default.Phone,
            modifier      = Modifier.fillMaxWidth(),
            enabled       = !isLoading,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction    = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            supportingText = {
                Text(
                    text  = "${phone.length}/11",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

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
            onTrailingIconClick = { passwordVisible = !passwordVisible },
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
                    viewModel.registerUser(email, password, name, phone)
                }
            ),
            // ── Password Strength ──────────────────────
            supportingText = {
                val (strengthText, strengthColor) = when {
                    password.length >= 8 &&
                            password.any { it.isDigit() } &&
                            password.any { it.isUpperCase() } ->
                        stringResource(R.string.password_strong) to
                                androidx.compose.ui.graphics.Color(0xFF2E7D32)

                    password.length >= 6 ->
                        stringResource(R.string.password_ok) to
                                androidx.compose.ui.graphics.Color(0xFFF57F17)

                    password.isNotEmpty() ->
                        stringResource(R.string.password_weak) to
                                androidx.compose.ui.graphics.Color(0xFFC62828)

                    else -> "" to androidx.compose.ui.graphics.Color.Transparent
                }
                if (strengthText.isNotEmpty()) {
                    Text(
                        text  = strengthText,
                        color = strengthColor,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(28.dp))

        // ── Register Button — AppButton component ──────
        AppButton(
            text        = stringResource(R.string.btn_register),
            loadingText = stringResource(R.string.btn_registering),
            onClick     = {
                keyboardController?.hide()
                viewModel.registerUser(email, password, name, phone)
            },
            isLoading = isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Login Link ─────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text  = stringResource(R.string.already_have_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(
                onClick = onNavigateToLogin,
                enabled = !isLoading
            ) {
                Text(
                    text  = stringResource(R.string.go_to_login),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}