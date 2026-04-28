package com.example.fix_my_ride.Features.MechanicFinder.domain.usecase


import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic
import com.example.fix_my_ride.Features.MechanicFinder.domain.repository.MechanicRepository
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import javax.inject.Inject

class GetMechanicProfileUseCase @Inject constructor(
    private val repository: MechanicRepository
) {
    suspend operator fun invoke(mechanicId: String): AuthResult<Mechanic> {
        return repository.getMechanicProfile(mechanicId)
    }
}