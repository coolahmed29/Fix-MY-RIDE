package com.example.fix_my_ride.Features.SpareParts.Domain.usecase

// Features/SpareParts/Domain/usecase/GetCategoriesUseCase.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.SpareParts.Domain.repository.SparePartsRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository     : SparePartsRepository,
    private val getTokenUseCase: GetTokenUseCase
) {
    suspend operator fun invoke(): AuthResult<List<String>> {
        val token = getTokenUseCase()

        if (token.isBlank())
            return AuthResult.Error("Session expire — login karein")

        return repository.getCategories(token)
    }
}