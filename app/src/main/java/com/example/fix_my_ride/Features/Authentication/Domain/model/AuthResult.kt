package com.example.fix_my_ride.Features.Authentication.Domain.model

// features/auth/domain/model/AuthResult.kt

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
    object Loading : AuthResult<Nothing>()
}