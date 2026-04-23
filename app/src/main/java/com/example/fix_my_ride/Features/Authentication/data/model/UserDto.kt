package com.example.fix_my_ride.Features.Authentication.data.model

// Firebase Firestore se data aata hai isme
// Default values zaroori hain — Firestore reflection use karta hai
data class UserDto(
    val uid       : String = "",
    val name      : String = "",
    val phone     : String = "",
    val email     : String = "",
    val token     : String = "",
    val createdAt : Long   = 0L
) {
    // ── Firestore DTO → Domain Model ─────────────────
    fun toDomain() =
        com.example.fix_my_ride.Features.Authentication.Domain.model.UserModel(
            uid       = uid,
            name      = name,
            phone     = phone,
            email     = email,
            token     = token,
            createdAt = createdAt
        )
}