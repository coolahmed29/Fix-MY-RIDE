package com.example.fix_my_ride.Features.Detailing.domain.usecase

// Features/Detailing/domain/usecase/GetAvailableSlotsUseCase.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.domain.model.TimeSlot
import com.example.fix_my_ride.Features.Detailing.domain.repository.BookingRepository
import javax.inject.Inject

class GetAvailableSlotsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(
        packageId : String,
        date      : String
    ): AuthResult<List<TimeSlot>> {
        if (packageId.isBlank())
            return AuthResult.Error("Package ID zaroori hai")
        if (date.isBlank())
            return AuthResult.Error("Date zaroori hai")
        return repository.getAvailableSlots(packageId, date)
    }
}