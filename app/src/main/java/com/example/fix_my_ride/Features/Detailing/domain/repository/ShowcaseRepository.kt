package com.example.fix_my_ride.Features.Detailing.domain.repository

// Features/Detailing/domain/repository/ShowcaseRepository.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.domain.model.Showcase
import kotlinx.coroutines.flow.Flow

interface ShowcaseRepository {
    suspend fun uploadShowcase(
        title       : String,
        description : String,
        imageUrls   : List<String>,
        offerText   : String
    ): AuthResult<Unit>

    fun getShowcases(): Flow<AuthResult<List<Showcase>>>
}