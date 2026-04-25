package com.example.fix_my_ride.Features.SpareParts.Domain.usecase

// Features/SpareParts/Domain/usecase/GetTokenUseCase.kt

import com.example.fix_my_ride.Features.Authentication.Domain.repository.AuthRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): String {
        return authRepository.getStoredToken() ?: ""
    }
}