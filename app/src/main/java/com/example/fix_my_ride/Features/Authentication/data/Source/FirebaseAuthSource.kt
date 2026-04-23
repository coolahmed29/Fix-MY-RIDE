package com.example.fix_my_ride.Features.Authentication.data.Source

// features/auth/data/source/FirebaseAuthSource.kt

import com.example.fix_my_ride.Features.Authentication.data.model.UserDto
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthSource @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val firestore    : FirebaseFirestore
) {
    companion object {
        private const val USERS_COLLECTION = "users"
    }

    // ── REGISTER ──────────────────────────────────────
    suspend fun register(
        email    : String,
        password : String,
        name     : String,
        phone    : String
    ): AuthResult<UserDto> {
        return try {

            // 1. Firebase Auth mein account banao
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .await()

            val firebaseUser = authResult.user
                ?: return AuthResult.Error("Registration failed")

            // 2. Token lo
            val token = firebaseUser
                .getIdToken(false)
                .await()
                .token
                ?: return AuthResult.Error("Token fetch nahi hua")

            // 3. DTO banao
            val userDto = UserDto(
                uid       = firebaseUser.uid,
                name      = name,
                phone     = phone,
                email     = email,
                token     = token,
                createdAt = System.currentTimeMillis()
            )

            // 4. Firestore mein save karo
            firestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .set(userDto)
                .await()

            AuthResult.Success(userDto)

        } catch (e: FirebaseAuthUserCollisionException) {
            AuthResult.Error("Yeh email pehle se registered hai")
        } catch (e: FirebaseAuthWeakPasswordException) {
            AuthResult.Error("Password bahut kamzor hai")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResult.Error("Email format sahi nahi")
        } catch (e: FirebaseNetworkException) {
            AuthResult.Error("Internet connection check karein")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Kuch ghalat hua")
        }
    }

    // ── LOGIN ─────────────────────────────────────────
    suspend fun login(
        email    : String,
        password : String
    ): AuthResult<UserDto> {
        return try {

            // 1. Login karo
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()

            val firebaseUser = authResult.user
                ?: return AuthResult.Error("Login failed")

            // 2. Fresh token lo
            val token = firebaseUser
                .getIdToken(true)   // true = force refresh
                .await()
                .token
                ?: return AuthResult.Error("Token refresh nahi hua")

            // 3. Firestore se user data lo
            val userDoc = firestore
                .collection(USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            val userDto = userDoc.toObject(UserDto::class.java)
                ?: return AuthResult.Error("User data nahi mila")

            AuthResult.Success(userDto.copy(token = token))

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResult.Error("Email ya password galat hai")
        } catch (e: FirebaseAuthInvalidUserException) {
            AuthResult.Error("Yeh account exist nahi karta")
        } catch (e: FirebaseNetworkException) {
            AuthResult.Error("Internet connection check karein")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Kuch ghalat hua")
        }
    }

    // ── IS LOGGED IN ──────────────────────────────────
    fun isUserLoggedIn(): Boolean =
        firebaseAuth.currentUser != null
}