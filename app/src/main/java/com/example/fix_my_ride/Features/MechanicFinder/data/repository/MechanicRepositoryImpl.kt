package com.example.fix_my_ride.Features.MechanicFinder.data.repository

import com.example.fix_my_ride.Features.MechanicFinder.data.model.toDto
import com.example.fix_my_ride.Features.MechanicFinder.data.source.MechanicFirebaseSource
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceRequest
import com.example.fix_my_ride.Features.MechanicFinder.domain.repository.MechanicRepository
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import android.util.Log

class MechanicRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MechanicRepository {

    private val firebaseSource = MechanicFirebaseSource(firestore)

    override suspend fun getNearbyMechanics(
        latitude: Double,
        longitude: Double,
        radiusKm: Double
    ): AuthResult<List<Mechanic>> {
        return try {
            Log.d("MechanicRepo", "Fetching mechanics at: $latitude, $longitude radius: $radiusKm km")
            val mechanics = firebaseSource.getNearbyMechanics(latitude, longitude, radiusKm)
            Log.d("MechanicRepo", "Found ${mechanics.size} mechanics within radius")
            AuthResult.Success(mechanics.map { it.toDomainModel() })
        } catch (e: Exception) {
            Log.e("MechanicRepo", "getNearbyMechanics error: ${e.message}", e)
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getMechanicProfile(mechanicId: String): AuthResult<Mechanic> {
        if (mechanicId.isBlank()) return AuthResult.Error("Mechanic ID cannot be empty")

        return try {
            Log.d("MechanicRepo", "Fetching profile for: $mechanicId")
            val mechanic = firebaseSource.getMechanicProfile(mechanicId)
            if (mechanic != null) {
                Log.d("MechanicRepo", "Profile found: ${mechanic.name}")
                AuthResult.Success(mechanic.toDomainModel())
            } else {
                Log.w("MechanicRepo", "Mechanic not found: $mechanicId")
                AuthResult.Error("Mechanic not found")
            }
        } catch (e: Exception) {
            Log.e("MechanicRepo", "getMechanicProfile error: ${e.message}", e)
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun sendServiceRequest(serviceRequest: ServiceRequest): AuthResult<ServiceRequest> {
        if (serviceRequest.userId.isBlank()) return AuthResult.Error("User ID is required")
        if (serviceRequest.mechanicId.isBlank()) return AuthResult.Error("Mechanic ID is required")
        // FIX: serviceType enum hai, String nahi — .name se check karo
        if (serviceRequest.serviceType.name.isBlank()) return AuthResult.Error("Service type is required")

        return try {
            // FIX: toDto() ab ServiceRequestDto.kt ki extension function hai — direct call karo
            val dto = serviceRequest.toDto()
            val result = firebaseSource.sendServiceRequest(dto)
            if (result != null) {
                Log.d("MechanicRepo", "Service request sent: ${result.id}")
                AuthResult.Success(result.toDomainModel())
            } else {
                AuthResult.Error("Failed to send service request")
            }
        } catch (e: Exception) {
            Log.e("MechanicRepo", "sendServiceRequest error: ${e.message}", e)
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getUserServiceRequests(userId: String): AuthResult<List<ServiceRequest>> {
        if (userId.isBlank()) return AuthResult.Error("User ID cannot be empty")

        return try {
            val requests = firebaseSource.getUserServiceRequests(userId)
                .map { it.toDomainModel() }
            Log.d("MechanicRepo", "Fetched ${requests.size} requests for user: $userId")
            AuthResult.Success(requests)
        } catch (e: Exception) {
            Log.e("MechanicRepo", "getUserServiceRequests error: ${e.message}", e)
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getServiceRequest(requestId: String): AuthResult<ServiceRequest> {
        if (requestId.isBlank()) return AuthResult.Error("Request ID cannot be empty")

        return try {
            val request = firebaseSource.getServiceRequest(requestId)
            if (request != null) {
                AuthResult.Success(request.toDomainModel())
            } else {
                AuthResult.Error("Service request not found")
            }
        } catch (e: Exception) {
            Log.e("MechanicRepo", "getServiceRequest error: ${e.message}", e)
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun updateServiceRequestStatus(
        requestId: String,
        status: String
    ): AuthResult<ServiceRequest> {
        if (requestId.isBlank()) return AuthResult.Error("Request ID cannot be empty")
        if (status.isBlank()) return AuthResult.Error("Status cannot be empty")

        return try {
            val success = firebaseSource.updateServiceRequestStatus(requestId, status)
            if (success) {
                val updatedRequest = firebaseSource.getServiceRequest(requestId)
                    ?: return AuthResult.Error("Status updated but request could not be fetched")
                AuthResult.Success(updatedRequest.toDomainModel())
            } else {
                AuthResult.Error("Failed to update service request status")
            }
        } catch (e: Exception) {
            Log.e("MechanicRepo", "updateServiceRequestStatus error: ${e.message}", e)
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun updateMechanicAvailability(
        mechanicId: String,
        isAvailable: Boolean
    ): AuthResult<Boolean> {
        if (mechanicId.isBlank()) return AuthResult.Error("Mechanic ID cannot be empty")

        return try {
            val success = firebaseSource.updateMechanicAvailability(mechanicId, isAvailable)
            Log.d("MechanicRepo", "Availability updated: $mechanicId -> $isAvailable")
            AuthResult.Success(success)
        } catch (e: Exception) {
            Log.e("MechanicRepo", "updateMechanicAvailability error: ${e.message}", e)
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override fun trackMechanicLocation(mechanicId: String): Flow<AuthResult<Pair<Double, Double>>> {
        if (mechanicId.isBlank()) {
            return flow {
                emit(AuthResult.Error("Mechanic ID cannot be empty"))
            }
        }

        return firebaseSource.trackMechanicLocation(mechanicId)
            .map { location ->
                if (location != null) {
                    if (location.first == 0.0 && location.second == 0.0) {
                        AuthResult.Error("Mechanic location is not available yet")
                    } else {
                        AuthResult.Success(location)
                    }
                } else {
                    AuthResult.Error("Location not available")
                }
            }
            .catch { e ->
                emit(AuthResult.Error(e.message ?: "Unknown error occurred"))
            }
    }

    override suspend fun cancelServiceRequest(requestId: String): AuthResult<Boolean> {
        if (requestId.isBlank()) return AuthResult.Error("Request ID cannot be empty")

        return try {
            val success = firebaseSource.cancelServiceRequest(requestId)
            if (success) {
                Log.d("MechanicRepo", "Request cancelled: $requestId")
                AuthResult.Success(true)
            } else {
                AuthResult.Error("Failed to cancel service request")
            }
        } catch (e: Exception) {
            Log.e("MechanicRepo", "cancelServiceRequest error: ${e.message}", e)
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun rateMechanic(
        requestId: String,
        rating: Double,
        feedback: String
    ): AuthResult<Boolean> {
        if (requestId.isBlank()) return AuthResult.Error("Request ID cannot be empty")
        if (rating < 1.0 || rating > 5.0) return AuthResult.Error("Rating must be between 1 and 5")

        return try {
            val success = firebaseSource.rateMechanic(requestId, rating, feedback)
            Log.d("MechanicRepo", "Mechanic rated for request: $requestId, rating: $rating")
            AuthResult.Success(success)
        } catch (e: Exception) {
            Log.e("MechanicRepo", "rateMechanic error: ${e.message}", e)
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getMechanicsByServiceType(
        serviceType: String,
        latitude: Double,
        longitude: Double,
        radiusKm: Double
    ): AuthResult<List<Mechanic>> {
        if (serviceType.isBlank()) return AuthResult.Error("Service type cannot be empty")

        return try {
            val mechanics = firebaseSource.getMechanicsByServiceType(
                serviceType, latitude, longitude, radiusKm
            ).map { it.toDomainModel() }
            Log.d("MechanicRepo", "Found ${mechanics.size} mechanics for: $serviceType")
            AuthResult.Success(mechanics)
        } catch (e: Exception) {
            Log.e("MechanicRepo", "getMechanicsByServiceType error: ${e.message}", e)
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun searchMechanics(query: String): AuthResult<List<Mechanic>> {
        if (query.isBlank()) return AuthResult.Success(emptyList())

        return try {
            val mechanics = firebaseSource.searchMechanics(query)
                .map { it.toDomainModel() }
            Log.d("MechanicRepo", "Search '$query' returned ${mechanics.size} results")
            AuthResult.Success(mechanics)
        } catch (e: Exception) {
            Log.e("MechanicRepo", "searchMechanics error: ${e.message}", e)
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }
}