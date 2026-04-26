package com.example.fix_my_ride.Features.Detailing.data.model

// Features/Detailing/data/model/TimeSlotDto.kt

import com.example.fix_my_ride.Features.Detailing.domain.model.TimeSlot
import com.google.firebase.firestore.PropertyName

data class TimeSlotDto(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id          : String  = "",

    @get:PropertyName("time")
    @set:PropertyName("time")
    var time        : String  = "",

    @get:PropertyName("is_available")
    @set:PropertyName("is_available")
    var isAvailable : Boolean = true,

    @get:PropertyName("date")
    @set:PropertyName("date")
    var date        : String  = "",

    @get:PropertyName("package_id")
    @set:PropertyName("package_id")
    var packageId   : String  = ""
) {
    fun toDomain(): TimeSlot = TimeSlot(
        id          = id,
        time        = time,
        isAvailable = isAvailable,
        date        = date,
        packageId   = packageId
    )
}