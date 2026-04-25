package com.example.fix_my_ride.Features.SpareParts.Domain.repository

// Features/SpareParts/Domain/repository/SparePartsRepository.kt

import com.example.fix_my_ride.Features.SpareParts.Domain.model.Part
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Vendor
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import kotlinx.coroutines.flow.Flow

interface SparePartsRepository {

    // ── Parts ─────────────────────────────────────────
    fun searchParts(
        query    : String,
        category : String,
        token    : String    // ← Token har request mein
    ): Flow<AuthResult<List<Part>>>

    // ── Vendors ───────────────────────────────────────
    suspend fun getVendorsForPart(
        partId : String,
        token  : String      // ← Token har request mein
    ): AuthResult<List<Vendor>>

    // ── Categories ────────────────────────────────────
    suspend fun getCategories(
        token : String
    ): AuthResult<List<String>>
}