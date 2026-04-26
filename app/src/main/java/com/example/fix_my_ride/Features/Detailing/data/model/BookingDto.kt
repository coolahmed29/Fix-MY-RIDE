package com.example.fix_my_ride.Features.Detailing.data.model

// Features/Detailing/data/model/BookingDto.kt

import com.example.fix_my_ride.Features.Detailing.domain.model.Booking
import com.example.fix_my_ride.Features.Detailing.domain.model.BookingStatus
import com.google.firebase.firestore.PropertyName

data class BookingDto(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id           : String = "",

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId       : String = "",

    @get:PropertyName("package_id")
    @set:PropertyName("package_id")
    var packageId    : String = "",

    @get:PropertyName("package_name")
    @set:PropertyName("package_name")
    var packageName  : String = "",

    @get:PropertyName("workshop_name")
    @set:PropertyName("workshop_name")
    var workshopName : String = "",

    @get:PropertyName("workshop_id")
    @set:PropertyName("workshop_id")
    var workshopId   : String = "",

    @get:PropertyName("date")
    @set:PropertyName("date")
    var date         : String = "",

    @get:PropertyName("time_slot")
    @set:PropertyName("time_slot")
    var timeSlot     : String = "",

    @get:PropertyName("price")
    @set:PropertyName("price")
    var price        : Double = 0.0,

    @get:PropertyName("status")
    @set:PropertyName("status")
    var status       : String = BookingStatus.PENDING.name,

    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    var createdAt    : Long   = 0L
) {
    fun toDomain(): Booking = Booking(
        id           = id,
        userId       = userId,
        packageId    = packageId,
        packageName  = packageName,
        workshopName = workshopName,
        workshopId   = workshopId,
        date         = date,
        timeSlot     = timeSlot,
        price        = price,
        status       = runCatching {
            BookingStatus.valueOf(status)
        }.getOrDefault(BookingStatus.PENDING),
        createdAt    = createdAt
    )
}