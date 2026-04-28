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

class MechanicFirebaseSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val mechanicsCollection = "mechanics"
    private val serviceRequestsCollection = "service_requests"

    // Get nearby mechanics
    suspend fun getNearbyMechanics(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ): List<MechanicDto> {
        return try {
            val allMechanics = firestore.collection(mechanicsCollection)
                .get()
                .await()
                .toObjects(MechanicDto::class.java)

            // Filter by distance
            allMechanics.filter { mechanic ->
                calculateDistance(latitude, longitude, mechanic.latitude, mechanic.longitude) <= radiusKm
            }.map { mechanic ->
                mechanic.copy(
                    distance = calculateDistance(latitude, longitude, mechanic.latitude, mechanic.longitude)
                )
            }.sortedBy { it.distance }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Get mechanic by ID
    suspend fun getMechanicProfile(mechanicId: String): MechanicDto? {
        return try {
            firestore.collection(mechanicsCollection)
                .document(mechanicId)
                .get()
                .await()
                .toObject(MechanicDto::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Send service request
    suspend fun sendServiceRequest(serviceRequest: ServiceRequestDto): ServiceRequestDto? {
        return try {
            val docRef = firestore.collection(serviceRequestsCollection).document()
            val requestWithId = serviceRequest.copy(id = docRef.id)
            docRef.set(requestWithId).await()
            requestWithId
        } catch (e: Exception) {
            null
        }
    }

    // Get user's service requests
    suspend fun getUserServiceRequests(userId: String): List<ServiceRequestDto> {
        return try {
            firestore.collection(serviceRequestsCollection)
                .whereEqualTo("user_id", userId)
                .get()
                .await()
                .toObjects(ServiceRequestDto::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Get specific service request
    suspend fun getServiceRequest(requestId: String): ServiceRequestDto? {
        return try {
            firestore.collection(serviceRequestsCollection)
                .document(requestId)
                .get()
                .await()
                .toObject(ServiceRequestDto::class.java)
        } catch (e: Exception) {
            null
        }
    }

    // Update service request status
    suspend fun updateServiceRequestStatus(
        requestId: String,
        status: String
    ): Boolean {
        return try {
            firestore.collection(serviceRequestsCollection)
                .document(requestId)
                .update("status", status)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Update mechanic availability
    suspend fun updateMechanicAvailability(
        mechanicId: String,
        isAvailable: Boolean
    ): Boolean {
        return try {
            firestore.collection(mechanicsCollection)
                .document(mechanicId)
                .update("is_available", isAvailable)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Track mechanic location in real-time
    fun trackMechanicLocation(mechanicId: String): Flow<Pair<Double, Double>?> = callbackFlow {
        val listener = firestore.collection(mechanicsCollection)
            .document(mechanicId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val mechanic = snapshot.toObject(MechanicDto::class.java)
                    if (mechanic != null) {
                        trySend(Pair(mechanic.latitude, mechanic.longitude))
                    }
                }
            }

        awaitClose {
            listener.remove()
        }
    }

    // Cancel service request
    suspend fun cancelServiceRequest(requestId: String): Boolean {
        return try {
            firestore.collection(serviceRequestsCollection)
                .document(requestId)
                .update("status", "CANCELLED")
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Rate mechanic
    suspend fun rateMechanic(
        requestId: String,
        rating: Double,
        feedback: String
    ): Boolean {
        return try {
            firestore.collection(serviceRequestsCollection)
                .document(requestId)
                .update(
                    mapOf(
                        "rating" to rating,
                        "feedback" to feedback,
                        "status" to "COMPLETED"
                    )
                )
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Get mechanics by service type
    suspend fun getMechanicsByServiceType(
        serviceType: String,
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ): List<MechanicDto> {
        return try {
            val allMechanics = firestore.collection(mechanicsCollection)
                .get()
                .await()
                .toObjects(MechanicDto::class.java)

            allMechanics.filter { mechanic ->
                mechanic.specializations.contains(serviceType) &&
                        calculateDistance(latitude, longitude, mechanic.latitude, mechanic.longitude) <= radiusKm
            }.map { mechanic ->
                mechanic.copy(
                    distance = calculateDistance(latitude, longitude, mechanic.latitude, mechanic.longitude)
                )
            }.sortedBy { it.distance }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Search mechanics by name
    suspend fun searchMechanics(query: String): List<MechanicDto> {
        return try {
            firestore.collection(mechanicsCollection)
                .get()
                .await()
                .toObjects(MechanicDto::class.java)
                .filter { mechanic ->
                    mechanic.name.contains(query, ignoreCase = true) ||
                            mechanic.workshopName?.contains(query, ignoreCase = true) == true
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Helper function to calculate distance between two coordinates
    private fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val r = 6371 // Earth's radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * asin(sqrt(a))
        return r * c
    }
}