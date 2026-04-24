package com.example.fix_my_ride.Features.Authentication.data.repository

// features/auth/data/repository/AuthRepositoryImpl.kt

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.fix_my_ride.Features.Authentication.Domain.repository.AuthRepository
import com.example.fix_my_ride.Features.Authentication.data.Source.FirebaseAuthSource
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Authentication.Domain.model.UserModel

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthSource : FirebaseAuthSource,
    private val dataStore          : DataStore<Preferences>
) : AuthRepository {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    // ── REGISTER ──────────────────────────────────────
    override suspend fun registerUser(
        email    : String,
        password : String,
        name     : String,
        phone    : String
    ): AuthResult<UserModel> {
        // FirebaseAuthSource se result lo
        val result = firebaseAuthSource.register(
            email, password, name, phone
        )

        // Success mein token save karo
        if (result is AuthResult.Success) {
            saveToken(result.data.token)
        }

        // DTO → Domain Model convert karo
        return when (result) {
            is AuthResult.Success ->
                AuthResult.Success(result.data.toDomain())
            is AuthResult.Error   ->
                AuthResult.Error(result.message)
            AuthResult.Loading    ->
                AuthResult.Loading
        }
    }

    // ── LOGIN ─────────────────────────────────────────
    override suspend fun loginUser(
        email    : String,
        password : String
    ): AuthResult<UserModel> {
        val result = firebaseAuthSource.login(email, password)

        if (result is AuthResult.Success) {
            saveToken(result.data.token)
        }

        return when (result) {
            is AuthResult.Success ->
                AuthResult.Success(result.data.toDomain())
            is AuthResult.Error   ->
                AuthResult.Error(result.message)
            AuthResult.Loading    ->
                AuthResult.Loading
        }
    }

    // ── TOKEN ─────────────────────────────────────────
    override suspend fun saveToken(token: String) {
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    override suspend fun getStoredToken(): String? {
        return try {
            dataStore.data
                .map { it[TOKEN_KEY] }
                .first()
        } catch (e: Exception) {
            null
        }
    }

    // ── SESSION ───────────────────────────────────────
    override fun isUserLoggedIn(): Boolean =
        firebaseAuthSource.isUserLoggedIn()
}