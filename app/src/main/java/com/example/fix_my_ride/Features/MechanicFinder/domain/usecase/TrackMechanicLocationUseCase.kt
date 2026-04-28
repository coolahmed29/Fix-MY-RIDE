package com.example.fix_my_ride.Features.MechanicFinder.domain.usecase


import com.example.fix_my_ride.Features.MechanicFinder.domain.repository.MechanicRepository
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TrackMechanicLocationUseCase @Inject constructor(
    private val repository: MechanicRepository
) {
    operator fun invoke(mechanicId: String): Flow<AuthResult<Pair<Double, Double>>> {
        return repository.trackMechanicLocation(mechanicId)
    }
}