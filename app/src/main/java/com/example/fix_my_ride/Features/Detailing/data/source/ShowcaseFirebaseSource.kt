package com.example.fix_my_ride.Features.Detailing.data.source

// Features/Detailing/data/source/ShowcaseFirebaseSource.kt

import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.Detailing.data.model.ShowcaseDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowcaseFirebaseSource @Inject constructor(
    private val firestore    : FirebaseFirestore,
    private val firebaseAuth : FirebaseAuth
) {
    companion object {
        private const val SHOWCASES = "showcases"
    }

    private val currentUserId get() =
        firebaseAuth.currentUser?.uid ?: ""

    // ── Upload Showcase ───────────────────────────────
    suspend fun uploadShowcase(
        title       : String,
        description : String,
        imageUrls   : List<String>,
        offerText   : String
    ): AuthResult<Unit> {
        return try {
            val docRef = firestore.collection(SHOWCASES).document()

            val showcaseDto = ShowcaseDto(
                id          = docRef.id,
                mechanicId  = currentUserId,
                workshopId  = "",
                title       = title,
                description = description,
                imageUrls   = imageUrls,
                offerText   = offerText,
                createdAt   = System.currentTimeMillis()
            )

            docRef.set(showcaseDto).await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Showcase upload nahi hua")
        }
    }

    // ── Get Showcases — Realtime ──────────────────────
    fun getShowcases(): Flow<AuthResult<List<ShowcaseDto>>> =
        callbackFlow {
            trySend(AuthResult.Loading)

            val listener = firestore
                .collection(SHOWCASES)
                .orderBy("created_at",
                    com.google.firebase.firestore.Query.Direction.DESCENDING
                )
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(AuthResult.Error(
                            error.message ?: "Showcases load nahi hue"
                        ))
                        return@addSnapshotListener
                    }

                    val showcases = snapshot?.documents
                        ?.mapNotNull { it.toObject(ShowcaseDto::class.java) }
                        ?: emptyList()

                    trySend(AuthResult.Success(showcases))
                }

            awaitClose { listener.remove() }
        }
}