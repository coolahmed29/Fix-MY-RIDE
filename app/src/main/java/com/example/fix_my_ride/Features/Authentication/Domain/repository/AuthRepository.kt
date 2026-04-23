package com.example.fix_my_ride.Features.Authentication.Domain.repository

// features/auth/domain/repository/AuthRepository.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Authentication.Domain.model.UserModel

interface AuthRepository {

    // ── Registration ──────────────────────────────────
    suspend fun registerUser(
        email    : String,
        password : String,
        name     : String,
        phone    : String
    ): AuthResult<UserModel>

    // ── Login ─────────────────────────────────────────
    suspend fun loginUser(
        email    : String,
        password : String
    ): AuthResult<UserModel>

    // ── Token ─────────────────────────────────────────
    suspend fun saveToken(token: String)
    suspend fun getStoredToken(): String?

    // ── Session ───────────────────────────────────────
    fun isUserLoggedIn(): Boolean
}