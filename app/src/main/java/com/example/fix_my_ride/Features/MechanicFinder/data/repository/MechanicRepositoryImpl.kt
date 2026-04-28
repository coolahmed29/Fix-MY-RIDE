package com.example.fix_my_ride.Features.MechanicFinder.data.repository


import com.example.fix_my_ride.Features.MechanicFinder.data.source.MechanicFirebaseSource
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceRequest
import com.example.fix_my_ride.Features.MechanicFinder.domain.repository.MechanicRepository
import com.example.fix_my_ride.Features.MechanicFinder.data.model.toDto
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MechanicRepositoryImpl @Inject constructor(
    private val firebaseSource: MechanicFirebaseSource
) : MechanicRepository {

    override suspend fun getNearbyMechanics(
        latitude: Double,
        longitude: Double,
        radiusKm: Double
    ): AuthResult<List<Mechanic>> {
        return try {
            val mechanics = firebaseSource.getNearbyMechanics(latitude, longitude, radiusKm)
                .map { it.toDomainModel() }
            AuthResult.Success(mechanics)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getMechanicProfile(mechanicId: String): AuthResult<Mechanic> {
        return try {
            val mechanic = firebaseSource.getMechanicProfile(mechanicId)
            if (mechanic != null) {
                AuthResult.Success(mechanic.toDomainModel())
            } else {
                AuthResult.Error("Mechanic not found")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun sendServiceRequest(serviceRequest: ServiceRequest): AuthResult<ServiceRequest> {
        return try {
            val result = firebaseSource.sendServiceRequest(serviceRequest.toDto())
            if (result != null) {
                AuthResult.Success(result.toDomainModel())
            } else {
                AuthResult.Error("Failed to send service request")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getUserServiceRequests(userId: String): AuthResult<List<ServiceRequest>> {
        return try {
            val requests = firebaseSource.getUserServiceRequests(userId)
                .map { it.toDomainModel() }
            AuthResult.Success(requests)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getServiceRequest(requestId: String): AuthResult<ServiceRequest> {
        return try {
            val request = firebaseSource.getServiceRequest(requestId)
            if (request != null) {
                AuthResult.Success(request.toDomainModel())
            } else {
                AuthResult.Error("Service request not found")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun updateServiceRequestStatus(
        requestId: String,
        status: String
    ): AuthResult<ServiceRequest> {
        return try {
            val success = firebaseSource.updateServiceRequestStatus(requestId, status)
            if (success) {
                val updatedRequest = firebaseSource.getServiceRequest(requestId)
                if (updatedRequest != null) {
                    AuthResult.Success(updatedRequest.toDomainModel())
                } else {
                    AuthResult.Error("Failed to fetch updated request")
                }
            } else {
                AuthResult.Error("Failed to update service request status")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun updateMechanicAvailability(
        mechanicId: String,
        isAvailable: Boolean
    ): AuthResult<Boolean> {
        return try {
            val success = firebaseSource.updateMechanicAvailability(mechanicId, isAvailable)
            AuthResult.Success(success)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override fun trackMechanicLocation(mechanicId: String): Flow<AuthResult<Pair<Double, Double>>> {
        return firebaseSource.trackMechanicLocation(mechanicId)
            .map { location ->
                if (location != null) {
                    AuthResult.Success(location)
                } else {
                    AuthResult.Error("Location not available")
                }
            }
            .catch { e ->
                emit(AuthResult.Error(e.message ?: "Unknown error occurred"))
            }
    }

    override suspend fun cancelServiceRequest(requestId: String): AuthResult<Boolean> {
        return try {
            val success = firebaseSource.cancelServiceRequest(requestId)
            AuthResult.Success(success)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun rateMechanic(
        requestId: String,
        rating: Double,
        feedback: String
    ): AuthResult<Boolean> {
        return try {
            val success = firebaseSource.rateMechanic(requestId, rating, feedback)
            AuthResult.Success(success)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun getMechanicsByServiceType(
        serviceType: String,
        latitude: Double,
        longitude: Double,
        radiusKm: Double
    ): AuthResult<List<Mechanic>> {
        return try {
            val mechanics = firebaseSource.getMechanicsByServiceType(serviceType, latitude, longitude, radiusKm)
                .map { it.toDomainModel() }
            AuthResult.Success(mechanics)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }

    override suspend fun searchMechanics(query: String): AuthResult<List<Mechanic>> {
        return try {
            val mechanics = firebaseSource.searchMechanics(query)
                .map { it.toDomainModel() }
            AuthResult.Success(mechanics)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown error occurred")
        }
    }
}