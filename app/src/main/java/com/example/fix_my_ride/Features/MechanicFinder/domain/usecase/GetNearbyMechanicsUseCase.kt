package com.example.fix_my_ride.Features.MechanicFinder.domain.usecase


import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic
import com.example.fix_my_ride.Features.MechanicFinder.domain.repository.MechanicRepository
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import javax.inject.Inject

class GetNearbyMechanicsUseCase @Inject constructor(
    private val repository: MechanicRepository
) {
    suspend operator fun invoke(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ): AuthResult<List<Mechanic>> {
        return repository.getNearbyMechanics(latitude, longitude, radiusKm)
    }
}