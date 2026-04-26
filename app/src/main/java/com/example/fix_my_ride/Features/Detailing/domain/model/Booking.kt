package com.example.fix_my_ride.Features.Detailing.domain.model

// Features/Detailing/domain/model/Booking.kt

data class Booking(
    val id           : String        = "",
    val userId       : String        = "",
    val packageId    : String        = "",
    val packageName  : String        = "",
    val workshopName : String        = "",
    val workshopId   : String        = "",
    val date         : String        = "",
    val timeSlot     : String        = "",
    val price        : Double        = 0.0,
    val status       : BookingStatus = BookingStatus.PENDING,
    val createdAt    : Long          = 0L
)