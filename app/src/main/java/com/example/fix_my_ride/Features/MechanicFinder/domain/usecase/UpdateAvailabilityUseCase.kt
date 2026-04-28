package com.example.fix_my_ride.Features.MechanicFinder.domain.usecase

import com.example.fix_my_ride.Features.MechanicFinder.domain.repository.MechanicRepository
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import javax.inject.Inject

class UpdateAvailabilityUseCase @Inject constructor(
    private val repository: MechanicRepository
) {
    suspend operator fun invoke(
        mechanicId: String,
        isAvailable: Boolean
    ): AuthResult<Boolean> {
        return repository.updateMechanicAvailability(mechanicId, isAvailable)
    }
}