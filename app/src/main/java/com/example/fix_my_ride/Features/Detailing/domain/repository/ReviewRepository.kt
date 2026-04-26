package com.example.fix_my_ride.Features.Detailing.domain.repository

// Features/Detailing/domain/repository/ReviewRepository.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.domain.model.Review

interface ReviewRepository {
    suspend fun submitReview(
        bookingId  : String,
        packageId  : String,
        rating     : Int,
        reviewText : String
    ): AuthResult<Unit>

    suspend fun getReviewsForPackage(
        packageId: String
    ): AuthResult<List<Review>>
}