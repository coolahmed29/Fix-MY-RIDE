package com.example.fix_my_ride.Features.Authentication.Presentation.ViewModel

// features/auth/presentation/AuthViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Authentication.Domain.model.UserModel
import com.example.fix_my_ride.Features.Authentication.Domain.usecase.LoginUserUseCase
import com.example.fix_my_ride.Features.Authentication.Domain.usecase.RegisterUserUseCase
import com.example.fix_my_ride.Features.Authentication.Domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUserUseCase : RegisterUserUseCase,
    private val loginUserUseCase    : LoginUserUseCase,
    private val authRepository      : AuthRepository
) : ViewModel() {

    // ── Register State ────────────────────────────────
    private val _registerState =
        MutableStateFlow<AuthResult<UserModel>?>(null)
    val registerState: StateFlow<AuthResult<UserModel>?> =
        _registerState.asStateFlow()

    // ── Login State ───────────────────────────────────
    private val _loginState =
        MutableStateFlow<AuthResult<UserModel>?>(null)
    val loginState: StateFlow<AuthResult<UserModel>?> =
        _loginState.asStateFlow()

    // ── Register ──────────────────────────────────────
    fun registerUser(
        email    : String,
        password : String,
        name     : String,
        phone    : String
    ) {
        viewModelScope.launch {
            _registerState.value = AuthResult.Loading
            _registerState.value =
                registerUserUseCase(email, password, name, phone)
        }
    }

    // ── Login ─────────────────────────────────────────
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthResult.Loading
            _loginState.value = loginUserUseCase(email, password)
        }
    }

    // ── Session Check ─────────────────────────────────
    fun isUserLoggedIn() = authRepository.isUserLoggedIn()

    // ── Reset ─────────────────────────────────────────
    fun resetRegisterState() { _registerState.value = null }
    fun resetLoginState()    { _loginState.value    = null }
}