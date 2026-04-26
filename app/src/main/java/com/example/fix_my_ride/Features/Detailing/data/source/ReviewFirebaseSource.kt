package com.example.fix_my_ride.Features.Detailing.data.source

// Features/Detailing/data/source/ReviewFirebaseSource.kt
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.data.model.ReviewDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewFirebaseSource @Inject constructor(
    private val firestore    : FirebaseFirestore,
    private val firebaseAuth : FirebaseAuth
) {
    companion object {
        private const val REVIEWS = "reviews"
    }

    private val currentUserId get() =
        firebaseAuth.currentUser?.uid ?: ""

    // ── Submit Review ─────────────────────────────────
    suspend fun submitReview(
        bookingId  : String,
        packageId  : String,
        rating     : Int,
        reviewText : String
    ): AuthResult<Unit> {
        return try {
            val docRef = firestore.collection(REVIEWS).document()

            val reviewDto = ReviewDto(
                id         = docRef.id,
                userId     = currentUserId,
                bookingId  = bookingId,
                packageId  = packageId,
                rating     = rating,
                reviewText = reviewText,
                createdAt  = System.currentTimeMillis()
            )

            docRef.set(reviewDto).await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Review submit nahi hua")
        }
    }

    // ── Get Reviews for Package ───────────────────────
    suspend fun getReviewsForPackage(
        packageId: String
    ): AuthResult<List<ReviewDto>> {
        return try {
            val snapshot = firestore
                .collection(REVIEWS)
                .whereEqualTo("package_id", packageId)
                .get()
                .await()

            val reviews = snapshot.documents
                .mapNotNull { it.toObject(ReviewDto::class.java) }

            AuthResult.Success(reviews)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Reviews load nahi hue")
        }
    }
}