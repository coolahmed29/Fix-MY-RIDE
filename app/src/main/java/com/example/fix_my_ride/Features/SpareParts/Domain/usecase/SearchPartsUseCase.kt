package com.example.fix_my_ride.Features.SpareParts.Domain.usecase

// Features/SpareParts/Domain/usecase/SearchPartsUseCase.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Part
import com.example.fix_my_ride.Features.SpareParts.Domain.repository.SparePartsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchPartsUseCase @Inject constructor(
    private val repository     : SparePartsRepository,
    private val getTokenUseCase: GetTokenUseCase
) {
    operator fun invoke(
        query    : String,
        category : String
    ): Flow<AuthResult<List<Part>>> = flow {

        emit(AuthResult.Loading)

        val token = getTokenUseCase()

        if (token.isBlank()) {
            emit(AuthResult.Error("Session expire — login karein"))
            return@flow
        }

        // ✅ Repository flow collect karke emit karo
        repository.searchParts(query, category, token)
            .collect { emit(it) }
    }
}