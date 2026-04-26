package com.example.fix_my_ride.Features.Detailing.domain.model

// Features/Detailing/domain/model/TimeSlot.kt

data class TimeSlot(
    val id          : String  = "",
    val time        : String  = "",   // "09:00 AM"
    val isAvailable : Boolean = true,
    val date        : String  = "",   // "2024-01-15"
    val packageId   : String  = ""
)