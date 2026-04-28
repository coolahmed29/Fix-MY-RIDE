package com.example.fix_my_ride.Features.MechanicFinder.domain.usecase


import com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceRequest
import com.example.fix_my_ride.Features.MechanicFinder.domain.repository.MechanicRepository
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import javax.inject.Inject

class SendServiceRequestUseCase @Inject constructor(
    private val repository: MechanicRepository
) {
    suspend operator fun invoke(
        serviceRequest: ServiceRequest
    ): AuthResult<ServiceRequest> {
        return repository.sendServiceRequest(serviceRequest)
    }
}