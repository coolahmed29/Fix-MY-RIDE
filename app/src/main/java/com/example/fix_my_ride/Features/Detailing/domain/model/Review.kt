package com.example.fix_my_ride.Features.Detailing.domain.model

// Features/Detailing/domain/model/Review.kt

data class Review(
    val id         : String = "",
    val userId     : String = "",
    val bookingId  : String = "",
    val packageId  : String = "",
    val rating     : Int    = 0,
    val reviewText : String = "",
    val createdAt  : Long   = 0L
)