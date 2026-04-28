package com.example.fix_my_ride.Features.MechanicFinder.domain.repository


import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceRequest
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import kotlinx.coroutines.flow.Flow

interface MechanicRepository {

    // Get nearby mechanics based on user location
    suspend fun getNearbyMechanics(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ): AuthResult<List<Mechanic>>

    // Get mechanic by ID
    suspend fun getMechanicProfile(mechanicId: String): AuthResult<Mechanic>

    // Send service request to mechanic
    suspend fun sendServiceRequest(serviceRequest: ServiceRequest): AuthResult<ServiceRequest>

    // Get user's service requests
    suspend fun getUserServiceRequests(userId: String): AuthResult<List<ServiceRequest>>

    // Get specific service request
    suspend fun getServiceRequest(requestId: String): AuthResult<ServiceRequest>

    // Update service request status
    suspend fun updateServiceRequestStatus(
        requestId: String,
        status: String
    ): AuthResult<ServiceRequest>

    // Update mechanic availability
    suspend fun updateMechanicAvailability(
        mechanicId: String,
        isAvailable: Boolean
    ): AuthResult<Boolean>

    // Track mechanic location in real-time
    fun trackMechanicLocation(mechanicId: String): Flow<AuthResult<Pair<Double, Double>>>

    // Cancel service request
    suspend fun cancelServiceRequest(requestId: String): AuthResult<Boolean>

    // Rate mechanic after service completion
    suspend fun rateMechanic(
        requestId: String,
        rating: Double,
        feedback: String
    ): AuthResult<Boolean>

    // Get filtered mechanics by service type
    suspend fun getMechanicsByServiceType(
        serviceType: String,
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ): AuthResult<List<Mechanic>>

    // Search mechanics by name
    suspend fun searchMechanics(query: String): AuthResult<List<Mechanic>>
}