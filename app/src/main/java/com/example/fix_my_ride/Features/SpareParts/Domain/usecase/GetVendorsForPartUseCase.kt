package com.example.fix_my_ride.Features.SpareParts.Domain.usecase

// Features/SpareParts/Domain/usecase/GetVendorsForPartUseCase.kt

import com.example.fix_my_ride.Features.SpareParts.Domain.model.Vendor
import com.example.fix_my_ride.Features.SpareParts.Domain.repository.SparePartsRepository
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import javax.inject.Inject

class GetVendorsForPartUseCase @Inject constructor(
    private val repository     : SparePartsRepository,
    private val getTokenUseCase: GetTokenUseCase
) {
    suspend operator fun invoke(
        partId: String
    ): AuthResult<List<Vendor>> {
        val token = getTokenUseCase()

        if (token.isBlank()) {
            return AuthResult.Error("Session expire ho gayi — login karein")
        }

        return repository.getVendorsForPart(partId, token)
    }
}