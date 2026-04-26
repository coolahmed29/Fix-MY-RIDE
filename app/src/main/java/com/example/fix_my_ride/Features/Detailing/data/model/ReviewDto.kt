package com.example.fix_my_ride.Features.Detailing.data.model

// Features/Detailing/data/model/ReviewDto.kt

import com.example.fix_my_ride.Features.Detailing.domain.model.Review
import com.google.firebase.firestore.PropertyName

data class ReviewDto(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id         : String = "",

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId     : String = "",

    @get:PropertyName("booking_id")
    @set:PropertyName("booking_id")
    var bookingId  : String = "",

    @get:PropertyName("package_id")
    @set:PropertyName("package_id")
    var packageId  : String = "",

    @get:PropertyName("rating")
    @set:PropertyName("rating")
    var rating     : Int    = 0,

    @get:PropertyName("review_text")
    @set:PropertyName("review_text")
    var reviewText : String = "",

    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    var createdAt  : Long   = 0L
) {
    fun toDomain(): Review = Review(
        id         = id,
        userId     = userId,
        bookingId  = bookingId,
        packageId  = packageId,
        rating     = rating,
        reviewText = reviewText,
        createdAt  = createdAt
    )
}