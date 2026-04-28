package com.example.fix_my_ride.Features.MechanicFinder.domain.model


data class ServiceRequest(
    val id: String,
    val userId: String,
    val mechanicId: String,
    val serviceType: ServiceType,
    val status: RequestStatus,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val carModel: String,
    val requestedTime: Long,
    val estimatedCost: Double?,
    val actualCost: Double?,
    val completedTime: Long?,
    val rating: Double?,
    val feedback: String?,
    val images: List<String> = emptyList(),
    val estimatedDuration: Int? = null,  // minutes
    val mechanicAcceptedTime: Long?,
    val mechanicArrivedTime: Long?,
    val serviceStartTime: Long?,
    val serviceEndTime: Long?
)

enum class RequestStatus {
    PENDING,           // Waiting for mechanic acceptance
    ACCEPTED,          // Mechanic accepted
    ON_THE_WAY,        // Mechanic is coming
    ARRIVED,           // Mechanic arrived at location
    IN_PROGRESS,       // Work started
    COMPLETED,         // Work completed
    CANCELLED,         // Cancelled by user or mechanic
    REJECTED           // Mechanic rejected
}