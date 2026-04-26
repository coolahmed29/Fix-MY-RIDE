package com.example.fix_my_ride.Features.Detailing.domain.usecase

// Features/Detailing/domain/usecase/SubmitReviewUseCase.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.domain.repository.BookingRepository
import com.example.fix_my_ride.Features.Detailing.domain.repository.ReviewRepository
import com.example.fix_my_ride.Features.Detailing.domain.model.BookingStatus
import javax.inject.Inject

class SubmitReviewUseCase @Inject constructor(
    private val reviewRepository  : ReviewRepository,
    private val bookingRepository : BookingRepository
) {
    suspend operator fun invoke(
        bookingId  : String,
        packageId  : String,
        rating     : Int,
        reviewText : String
    ): AuthResult<Unit> {
        if (rating == 0)
            return AuthResult.Error("Rating zaroori hai")
        if (rating !in 1..5)
            return AuthResult.Error("Rating 1-5 ke beech honi chahiye")
        return reviewRepository.submitReview(
            bookingId  = bookingId,
            packageId  = packageId,
            rating     = rating,
            reviewText = reviewText
        )
    }
}