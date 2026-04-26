package com.example.fix_my_ride.Features.Detailing.domain.usecase

// Features/Detailing/domain/usecase/CreateBookingUseCase.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.domain.model.Booking
import com.example.fix_my_ride.Features.Detailing.domain.repository.BookingRepository
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(
        packageId    : String,
        packageName  : String,
        price        : Double,
        date         : String,
        timeSlot     : String,
        workshopName : String,
        workshopId   : String
    ): AuthResult<Booking> {
        if (packageId.isBlank())
            return AuthResult.Error("Package ID missing")
        if (date.isBlank())
            return AuthResult.Error("Date select karein")
        if (timeSlot.isBlank())
            return AuthResult.Error("Time slot select karein")
        return repository.createBooking(
            packageId    = packageId,
            packageName  = packageName,
            price        = price,
            date         = date,
            timeSlot     = timeSlot,
            workshopName = workshopName,
            workshopId   = workshopId
        )
    }
}