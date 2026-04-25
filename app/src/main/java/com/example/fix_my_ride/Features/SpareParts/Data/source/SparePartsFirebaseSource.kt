package com.example.fix_my_ride.Features.SpareParts.Data.source

// Features/SpareParts/Data/source/SparePartsFirebaseSource.kt

import com.example.fix_my_ride.Features.SpareParts.Data.model.PartDto
import com.example.fix_my_ride.Features.SpareParts.Data.model.VendorDto
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SparePartsFirebaseSource @Inject constructor(
    private val firestore    : FirebaseFirestore,
    private val firebaseAuth : FirebaseAuth
) {
    companion object {
        private const val PARTS_COLLECTION   = "parts"
        private const val VENDORS_COLLECTION = "vendors"
    }

    // ── Token Verify karo ─────────────────────────────
    private suspend fun verifyToken(token: String): Boolean {
        return try {
            // Firebase current user ka fresh token lo
            val currentToken = firebaseAuth.currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token

            // Token match karo
            currentToken != null && firebaseAuth.currentUser != null
        } catch (e: Exception) {
            false
        }
    }

    // ── Search Parts ──────────────────────────────────
    fun searchParts(
        query    : String,
        category : String,
        token    : String
    ): Flow<AuthResult<List<PartDto>>> = callbackFlow {

        // Token verify karo pehle
        if (!verifyToken(token)) {
            trySend(AuthResult.Error("Unauthorized — Please login again"))
            close()
            return@callbackFlow
        }

        trySend(AuthResult.Loading)

        var firestoreQuery: Query = firestore.collection(PARTS_COLLECTION)

        // Category filter
        if (category.isNotBlank() && category != "All") {
            firestoreQuery = firestoreQuery
                .whereEqualTo("category", category)
        }

        val listener = firestoreQuery
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(AuthResult.Error(
                        error.message ?: "Parts fetch nahi hue"
                    ))
                    return@addSnapshotListener
                }

                val parts = snapshot?.documents
                    ?.mapNotNull { it.toObject(PartDto::class.java) }
                    ?.filter { part ->
                        // Client side search filter
                        query.isBlank() ||
                                part.name.contains(query, ignoreCase = true) ||
                                part.category.contains(query, ignoreCase = true) ||
                                part.carModel.contains(query, ignoreCase = true)
                    }
                    ?: emptyList()

                trySend(AuthResult.Success(parts))
            }

        awaitClose { listener.remove() }
    }

    // ── Get Vendors for Part ──────────────────────────
    suspend fun getVendorsForPart(
        partId : String,
        token  : String
    ): AuthResult<List<VendorDto>> {
        // Token verify
        if (!verifyToken(token)) {
            return AuthResult.Error("Unauthorized — Please login again")
        }

        return try {
            val snapshot = firestore
                .collection(VENDORS_COLLECTION)
                .whereEqualTo("partId", partId)
                .get()
                .await()

            val vendors = snapshot.documents
                .mapNotNull { it.toObject(VendorDto::class.java) }
                .sortedBy { it.price }  // Default: price se sort

            AuthResult.Success(vendors)

        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Vendors fetch nahi hue")
        }
    }

    // ── Get Categories ────────────────────────────────
    suspend fun getCategories(token: String): AuthResult<List<String>> {
        if (!verifyToken(token)) {
            return AuthResult.Error("Unauthorized")
        }

        return try {
            val snapshot = firestore
                .collection(PARTS_COLLECTION)
                .get()
                .await()

            val categories = snapshot.documents
                .mapNotNull { it.getString("category") }
                .distinct()
                .sorted()

            AuthResult.Success(listOf("All") + categories)

        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Categories fetch nahi huin")
        }
    }
}