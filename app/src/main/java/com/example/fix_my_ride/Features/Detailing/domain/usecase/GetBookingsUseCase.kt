package com.example.fix_my_ride.Features.Detailing.domain.usecase

// Features/Detailing/domain/usecase/GetBookingsUseCase.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.domain.model.Booking
import com.example.fix_my_ride.Features.Detailing.domain.model.BookingStatus
import com.example.fix_my_ride.Features.Detailing.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookingsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    operator fun invoke(
        status: BookingStatus
    ): Flow<AuthResult<List<Booking>>> =
        repository.getBookings(status)
}