package com.example.fix_my_ride.Features.MechanicFinder.data.source


import com.example.fix_my_ride.Features.MechanicFinder.data.model.MechanicDto
import com.example.fix_my_ride.Features.MechanicFinder.data.model.ServiceRequestDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.*
import android.util.Log

// FIX: @Inject constructor add kiya taake Hilt properly inject kar sake
class MechanicFirebaseSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val mechanicsCollection = "mechanics"
    private val serviceRequestsCollection = "service_requests"

    // ── Get Nearby Mechanics ───────────────────────
    suspend fun getNearbyMechanics(
        latitude: Double,
        longitude: Double,
        radiusKm: Double
    ): List<MechanicDto> {
        return try {
            Log.d("MechanicFirebaseSource", "Fetching mechanics from Firestore...")

            val snapshot = firestore.collection(mechanicsCollection)
                .get()
                .await()

            Log.d("MechanicFirebaseSource", "Total docs fetched: ${snapshot.size()}")

            snapshot.documents.mapNotNull { doc ->
                try {
                    val dto = doc.toObject(MechanicDto::class.java) ?: return@mapNotNull null

                    // FIX: mechanic ki lat/lon null ho toh skip karo, user location use mat karo
                    val mechLat = dto.latitude ?: run {
                        Log.w("MechanicFirebaseSource", "Skipping ${doc.id}: latitude missing")
                        return@mapNotNull null
                    }
                    val mechLon = dto.longitude ?: run {
                        Log.w("MechanicFirebaseSource", "Skipping ${doc.id}: longitude missing")
                        return@mapNotNull null
                    }

                    val distance = calculateDistance(mechLat, mechLon, latitude, longitude)

                    if (distance <= radiusKm) dto.copy(distance = distance) else null
                } catch (e: Exception) {
                    Log.e("MechanicFirebaseSource", "Error parsing doc ${doc.id}: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("MechanicFirebaseSource", "getNearbyMechanics error: ${e.message}", e)
            emptyList()
        }
    }

    // ── Get Mechanic Profile ───────────────────────
    suspend fun getMechanicProfile(mechanicId: String): MechanicDto? {
        return try {
            Log.d("MechanicFirebaseSource", "Fetching profile for: $mechanicId")

            val snapshot = firestore.collection(mechanicsCollection)
                .document(mechanicId)
                .get()
                .await()

            if (!snapshot.exists()) {
                Log.w("MechanicFirebaseSource", "No document found for: $mechanicId")
                return null
            }

            val dto = snapshot.toObject(MechanicDto::class.java)
            Log.d("MechanicFirebaseSource", "Profile fetched: ${dto?.name}")
            dto
        } catch (e: Exception) {
            Log.e("MechanicFirebaseSource", "getMechanicProfile error: ${e.message}", e)
            null
        }
    }

    // ── Send Service Request ────────────────────────
    suspend fun sendServiceRequest(serviceRequest: ServiceRequestDto): ServiceRequestDto? {
        return try {
            Log.d("MechanicFirebaseSource", "Saving service request: ${serviceRequest.id}")

            val docRef = firestore.collection(serviceRequestsCollection)
                .document(serviceRequest.id)

            docRef.set(serviceRequest).await()
            Log.d("MechanicFirebaseSource", "Request saved: ${serviceRequest.id}")

            // Saved object wapas fetch karo confirm karne ke liye
            docRef.get().await().toObject(ServiceRequestDto::class.java)
        } catch (e: Exception) {
            Log.e("MechanicFirebaseSource", "sendServiceRequest error: ${e.message}", e)
            null
        }
    }

    // ── Get User Service Requests ───────────────────
    suspend fun getUserServiceRequests(userId: String): List<ServiceRequestDto> {
        return try {
            Log.d("MechanicFirebaseSource", "Fetching requests for user: $userId")

            val snapshot = firestore.collection(serviceRequestsCollection)
                .whereEqualTo("user_id", userId)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(ServiceRequestDto::class.java)
            }
        } catch (e: Exception) {
            Log.e("MechanicFirebaseSource", "getUserServiceRequests error: ${e.message}", e)
            emptyList()
        }
    }

    // ── Get Service Request ─────────────────────────
    suspend fun getServiceRequest(requestId: String): ServiceRequestDto? {
        return try {
            Log.d("MechanicFirebaseSource", "Fetching request: $requestId")

            val snapshot = firestore.collection(serviceRequestsCollection)
                .document(requestId)
                .get()
                .await()

            if (!snapshot.exists()) {
                Log.w("MechanicFirebaseSource", "Request not found: $requestId")
                return null
            }

            snapshot.toObject(ServiceRequestDto::class.java)
        } catch (e: Exception) {
            Log.e("MechanicFirebaseSource", "getServiceRequest error: ${e.message}", e)
            null
        }
    }

    // ── Update Service Request Status ───────────────
    suspend fun updateServiceRequestStatus(requestId: String, status: String): Boolean {
        return try {
            Log.d("MechanicFirebaseSource", "Updating status: $requestId -> $status")

            firestore.collection(serviceRequestsCollection)
                .document(requestId)
                .update("status", status)
                .await()

            Log.d("MechanicFirebaseSource", "Status updated successfully")
            true
        } catch (e: Exception) {
            Log.e("MechanicFirebaseSource", "updateServiceRequestStatus error: ${e.message}", e)
            false
        }
    }

    // ── Update Mechanic Availability ────────────────
    suspend fun updateMechanicAvailability(mechanicId: String, isAvailable: Boolean): Boolean {
        return try {
            Log.d("MechanicFirebaseSource", "Updating availability: $mechanicId -> $isAvailable")

            firestore.collection(mechanicsCollection)
                .document(mechanicId)
                .update("is_available", isAvailable)
                .await()

            true
        } catch (e: Exception) {
            Log.e("MechanicFirebaseSource", "updateMechanicAvailability error: ${e.message}", e)
            false
        }
    }

    // ── Track Mechanic Location ─────────────────────
    fun trackMechanicLocation(mechanicId: String): Flow<Pair<Double, Double>?> = callbackFlow {
        Log.d("MechanicFirebaseSource", "Starting location tracking for: $mechanicId")

        val listener = firestore.collection(mechanicsCollection)
            .document(mechanicId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("MechanicFirebaseSource", "Location tracking error: ${error.message}")
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    try {
                        // FIX: null check properly karo, 0.0 default silently pass hoti thi
                        val lat = snapshot.getDouble("latitude")
                        val lon = snapshot.getDouble("longitude")

                        if (lat != null && lon != null) {
                            Log.d("MechanicFirebaseSource", "Location update: $lat, $lon")
                            trySend(Pair(lat, lon))
                        } else {
                            Log.w("MechanicFirebaseSource", "Location fields missing for: $mechanicId")
                            trySend(null)
                        }
                    } catch (e: Exception) {
                        Log.e("MechanicFirebaseSource", "Error parsing location: ${e.message}")
                        trySend(null)
                    }
                } else {
                    Log.w("MechanicFirebaseSource", "Snapshot missing or doesn't exist")
                    trySend(null)
                }
            }

        awaitClose {
            Log.d("MechanicFirebaseSource", "Stopping location tracking for: $mechanicId")
            listener.remove()
        }
    }

    // ── Cancel Service Request ──────────────────────
    // FIX: delete ki bajaye status "cancelled" set karo — history preserve hoti hai
    suspend fun cancelServiceRequest(requestId: String): Boolean {
        return try {
            Log.d("MechanicFirebaseSource", "Cancelling request: $requestId")

            firestore.collection(serviceRequestsCollection)
                .document(requestId)
                .update(
                    mapOf(
                        "status" to "cancelled",
                        "cancelled_at" to System.currentTimeMillis()
                    )
                )
                .await()

            Log.d("MechanicFirebaseSource", "Request cancelled: $requestId")
            true
        } catch (e: Exception) {
            Log.e("MechanicFirebaseSource", "cancelServiceRequest error: ${e.message}", e)
            false
        }
    }

    // ── Rate Mechanic ───────────────────────────────
    suspend fun rateMechanic(requestId: String, rating: Double, feedback: String): Boolean {
        return try {
            Log.d("MechanicFirebaseSource", "Rating mechanic for request: $requestId, rating: $rating")

            firestore.collection(serviceRequestsCollection)
                .document(requestId)
                .update(
                    mapOf(
                        "rating" to rating,
                        "feedback" to feedback,
                        "rated_at" to System.currentTimeMillis()
                    )
                )
                .await()

            true
        } catch (e: Exception) {
            Log.e("MechanicFirebaseSource", "rateMechanic error: ${e.message}", e)
            false
        }
    }

    // ── Get Mechanics by Service Type ───────────────
    suspend fun getMechanicsByServiceType(
        serviceType: String,
        latitude: Double,
        longitude: Double,
        radiusKm: Double
    ): List<MechanicDto> {
        return try {
            Log.d("MechanicFirebaseSource", "Fetching mechanics for service: $serviceType")

            val snapshot = firestore.collection(mechanicsCollection)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                try {
                    val dto = doc.toObject(MechanicDto::class.java) ?: return@mapNotNull null

                    // FIX: null lat/lon skip karo
                    val mechLat = dto.latitude ?: return@mapNotNull null
                    val mechLon = dto.longitude ?: return@mapNotNull null

                    val hasService = dto.specializations?.any {
                        it.equals(serviceType, ignoreCase = true)
                    } ?: false

                    val distance = calculateDistance(mechLat, mechLon, latitude, longitude)

                    if (hasService && distance <= radiusKm) dto.copy(distance = distance) else null
                } catch (e: Exception) {
                    Log.e("MechanicFirebaseSource", "Error parsing doc ${doc.id}: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("MechanicFirebaseSource", "getMechanicsByServiceType error: ${e.message}", e)
            emptyList()
        }
    }

    // ── Search Mechanics ────────────────────────────
    // FIX: specializations bhi search mein include kiya
    suspend fun searchMechanics(query: String): List<MechanicDto> {
        return try {
            Log.d("MechanicFirebaseSource", "Searching mechanics: '$query'")

            val snapshot = firestore.collection(mechanicsCollection)
                .get()
                .await()

            val lowerQuery = query.lowercase()

            snapshot.documents.mapNotNull { doc ->
                try {
                    val dto = doc.toObject(MechanicDto::class.java) ?: return@mapNotNull null

                    val matchesName = dto.name?.lowercase()?.contains(lowerQuery) ?: false
                    val matchesAddress = dto.workshop_address?.lowercase()?.contains(lowerQuery) ?: false
                    // FIX: specializations bhi match karo
                    val matchesSpecialization = dto.specializations?.any {
                        it.lowercase().contains(lowerQuery)
                    } ?: false

                    if (matchesName || matchesAddress || matchesSpecialization) dto else null
                } catch (e: Exception) {
                    Log.e("MechanicFirebaseSource", "Error parsing doc ${doc.id}: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("MechanicFirebaseSource", "searchMechanics error: ${e.message}", e)
            emptyList()
        }
    }

    // ── Helper: Calculate Distance (Haversine) ──────
    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)

        return r * 2 * asin(sqrt(a))
    }
}