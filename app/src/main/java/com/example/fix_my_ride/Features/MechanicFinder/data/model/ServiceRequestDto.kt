package com.example.fix_my_ride.Features.MechanicFinder.data.model


import com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceRequest
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.RequestStatus
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceType
import com.google.firebase.firestore.PropertyName

data class ServiceRequestDto(
    @PropertyName("id")
    val id: String = "",

    @PropertyName("user_id")
    val userId: String = "",

    @PropertyName("mechanic_id")
    val mechanicId: String = "",

    @PropertyName("service_type")
    val serviceType: String = "",

    @PropertyName("status")
    val status: String = "PENDING",

    @PropertyName("description")
    val description: String = "",

    @PropertyName("latitude")
    val latitude: Double = 0.0,

    @PropertyName("longitude")
    val longitude: Double = 0.0,

    @PropertyName("address")
    val address: String = "",

    @PropertyName("car_model")
    val carModel: String = "",

    @PropertyName("requested_time")
    val requestedTime: Long = 0L,

    @PropertyName("estimated_cost")
    val estimatedCost: Double? = null,

    @PropertyName("actual_cost")
    val actualCost: Double? = null,

    @PropertyName("completed_time")
    val completedTime: Long? = null,

    @PropertyName("rating")
    val rating: Double? = null,

    @PropertyName("feedback")
    val feedback: String? = null,

    @PropertyName("images")
    val images: List<String> = emptyList(),

    @PropertyName("estimated_duration")
    val estimatedDuration: Int? = null,

    @PropertyName("mechanic_accepted_time")
    val mechanicAcceptedTime: Long? = null,

    @PropertyName("mechanic_arrived_time")
    val mechanicArrivedTime: Long? = null,

    @PropertyName("service_start_time")
    val serviceStartTime: Long? = null,

    @PropertyName("service_end_time")
    val serviceEndTime: Long? = null
) {
    fun toDomainModel(): ServiceRequest {
        return ServiceRequest(
            id = id,
            userId = userId,
            mechanicId = mechanicId,
            serviceType = ServiceType.fromString(serviceType),
            status = try {
                RequestStatus.valueOf(status)
            } catch (e: Exception) {
                RequestStatus.PENDING
            },
            description = description,
            latitude = latitude,
            longitude = longitude,
            address = address,
            carModel = carModel,
            requestedTime = requestedTime,
            estimatedCost = estimatedCost,
            actualCost = actualCost,
            completedTime = completedTime,
            rating = rating,
            feedback = feedback,
            images = images,
            estimatedDuration = estimatedDuration,
            mechanicAcceptedTime = mechanicAcceptedTime,
            mechanicArrivedTime = mechanicArrivedTime,
            serviceStartTime = serviceStartTime,
            serviceEndTime = serviceEndTime
        )
    }
}

fun ServiceRequest.toDto(): ServiceRequestDto {
    return ServiceRequestDto(
        id = id,
        userId = userId,
        mechanicId = mechanicId,
        serviceType = serviceType.name,
        status = status.name,
        description = description,
        latitude = latitude,
        longitude = longitude,
        address = address,
        carModel = carModel,
        requestedTime = requestedTime,
        estimatedCost = estimatedCost,
        actualCost = actualCost,
        completedTime = completedTime,
        rating = rating,
        feedback = feedback,
        images = images,
        estimatedDuration = estimatedDuration,
        mechanicAcceptedTime = mechanicAcceptedTime,
        mechanicArrivedTime = mechanicArrivedTime,
        serviceStartTime = serviceStartTime,
        serviceEndTime = serviceEndTime
    )
}