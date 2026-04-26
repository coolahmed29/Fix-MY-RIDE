package com.example.fix_my_ride.Features.Detailing.data.repository

// Features/Detailing/data/repository/BookingRepositoryImpl.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.data.source.BookingFirebaseSource
import com.example.fix_my_ride.Features.Detailing.domain.model.Booking
import com.example.fix_my_ride.Features.Detailing.domain.model.BookingStatus
import com.example.fix_my_ride.Features.Detailing.domain.model.TimeSlot
import com.example.fix_my_ride.Features.Detailing.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepositoryImpl @Inject constructor(
    private val source: BookingFirebaseSource
) : BookingRepository {

    override suspend fun getAvailableSlots(
        packageId : String,
        date      : String
    ): AuthResult<List<TimeSlot>> {
        val result = source.getAvailableSlots(packageId, date)
        return when (result) {
            is AuthResult.Success ->
                AuthResult.Success(result.data.map { it.toDomain() })
            is AuthResult.Error   -> AuthResult.Error(result.message)
            AuthResult.Loading    -> AuthResult.Loading
        }
    }

    override suspend fun createBooking(
        packageId    : String,
        packageName  : String,
        price        : Double,
        date         : String,
        timeSlot     : String,
        workshopName : String,
        workshopId   : String
    ): AuthResult<Booking> {
        val result = source.createBooking(
            packageId    = packageId,
            packageName  = packageName,
            price        = price,
            date         = date,
            timeSlot     = timeSlot,
            workshopName = workshopName,
            workshopId   = workshopId
        )
        return when (result) {
            is AuthResult.Success ->
                AuthResult.Success(result.data.toDomain())
            is AuthResult.Error   -> AuthResult.Error(result.message)
            AuthResult.Loading    -> AuthResult.Loading
        }
    }

    override fun getBookings(
        status: BookingStatus
    ): Flow<AuthResult<List<Booking>>> =
        source.getBookings(status).map { result ->
            when (result) {
                is AuthResult.Success ->
                    AuthResult.Success(result.data.map { it.toDomain() })
                is AuthResult.Error   -> AuthResult.Error(result.message)
                AuthResult.Loading    -> AuthResult.Loading
            }
        }

    override suspend fun updateBookingStatus(
        bookingId : String,
        status    : BookingStatus
    ): AuthResult<Unit> = source.updateBookingStatus(bookingId, status)
}