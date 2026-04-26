package com.example.fix_my_ride.Features.Detailing.data.repository

// Features/Detailing/data/repository/ReviewRepositoryImpl.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.data.source.ReviewFirebaseSource
import com.example.fix_my_ride.Features.Detailing.domain.model.Review
import com.example.fix_my_ride.Features.Detailing.domain.repository.ReviewRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepositoryImpl @Inject constructor(
    private val source: ReviewFirebaseSource
) : ReviewRepository {

    override suspend fun submitReview(
        bookingId  : String,
        packageId  : String,
        rating     : Int,
        reviewText : String
    ): AuthResult<Unit> =
        source.submitReview(bookingId, packageId, rating, reviewText)

    override suspend fun getReviewsForPackage(
        packageId: String
    ): AuthResult<List<Review>> {
        val result = source.getReviewsForPackage(packageId)
        return when (result) {
            is AuthResult.Success ->
                AuthResult.Success(result.data.map { it.toDomain() })
            is AuthResult.Error   -> AuthResult.Error(result.message)
            AuthResult.Loading    -> AuthResult.Loading
        }
    }
}