package com.example.fix_my_ride.Features.Detailing.domain.repository

// Features/Detailing/domain/repository/BookingRepository.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.domain.model.Booking
import com.example.fix_my_ride.Features.Detailing.domain.model.BookingStatus
import com.example.fix_my_ride.Features.Detailing.domain.model.TimeSlot
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    suspend fun getAvailableSlots(
        packageId : String,
        date      : String
    ): AuthResult<List<TimeSlot>>

    suspend fun createBooking(
        packageId    : String,
        packageName  : String,
        price        : Double,
        date         : String,
        timeSlot     : String,
        workshopName : String,
        workshopId   : String
    ): AuthResult<Booking>

    fun getBookings(
        status: BookingStatus
    ): Flow<AuthResult<List<Booking>>>

    suspend fun updateBookingStatus(
        bookingId : String,
        status    : BookingStatus
    ): AuthResult<Unit>
}