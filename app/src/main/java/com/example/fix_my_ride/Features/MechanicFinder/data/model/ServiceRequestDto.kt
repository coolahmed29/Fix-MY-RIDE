package com.example.fix_my_ride.Features.MechanicFinder.data.model

import com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceRequest
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.RequestStatus
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceType
import com.google.firebase.firestore.PropertyName

data class ServiceRequestDto(
    @get:PropertyName("id") @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("user_id") @set:PropertyName("user_id")
    var user_id: String = "",

    @get:PropertyName("mechanic_id") @set:PropertyName("mechanic_id")
    var mechanic_id: String = "",

    @get:PropertyName("service_type") @set:PropertyName("service_type")
    var service_type: String = "",

    @get:PropertyName("status") @set:PropertyName("status")
    var status: String = "PENDING",

    @get:PropertyName("description") @set:PropertyName("description")
    var description: String = "",

    @get:PropertyName("latitude") @set:PropertyName("latitude")
    var latitude: Double = 0.0,

    @get:PropertyName("longitude") @set:PropertyName("longitude")
    var longitude: Double = 0.0,

    @get:PropertyName("address") @set:PropertyName("address")
    var address: String = "",

    @get:PropertyName("car_model") @set:PropertyName("car_model")
    var car_model: String = "",

    @get:PropertyName("requested_time") @set:PropertyName("requested_time")
    var requested_time: Long = 0L,

    @get:PropertyName("estimated_cost") @set:PropertyName("estimated_cost")
    var estimated_cost: Double? = null,

    @get:PropertyName("actual_cost") @set:PropertyName("actual_cost")
    var actual_cost: Double? = null,

    @get:PropertyName("completed_time") @set:PropertyName("completed_time")
    var completed_time: Long? = null,

    @get:PropertyName("rating") @set:PropertyName("rating")
    var rating: Double? = null,

    @get:PropertyName("feedback") @set:PropertyName("feedback")
    var feedback: String? = null,

    @get:PropertyName("images") @set:PropertyName("images")
    var images: List<String> = emptyList(),

    @get:PropertyName("estimated_duration") @set:PropertyName("estimated_duration")
    var estimated_duration: Int? = null,

    @get:PropertyName("mechanic_accepted_time") @set:PropertyName("mechanic_accepted_time")
    var mechanic_accepted_time: Long? = null,

    @get:PropertyName("mechanic_arrived_time") @set:PropertyName("mechanic_arrived_time")
    var mechanic_arrived_time: Long? = null,

    @get:PropertyName("service_start_time") @set:PropertyName("service_start_time")
    var service_start_time: Long? = null,

    @get:PropertyName("service_end_time") @set:PropertyName("service_end_time")
    var service_end_time: Long? = null
) {
    constructor() : this(id = "")

    fun toDomainModel(): ServiceRequest {
        return ServiceRequest(
            id = id,
            userId = user_id,
            mechanicId = mechanic_id,
            serviceType = ServiceType.fromString(service_type),
            status = try {
                RequestStatus.valueOf(status.uppercase())
            } catch (e: Exception) {
                RequestStatus.PENDING
            },
            description = description,
            latitude = latitude,
            longitude = longitude,
            address = address,
            carModel = car_model,
            requestedTime = requested_time,
            estimatedCost = estimated_cost,
            actualCost = actual_cost,
            completedTime = completed_time,
            rating = rating,
            feedback = feedback,
            images = images,
            estimatedDuration = estimated_duration,
            mechanicAcceptedTime = mechanic_accepted_time,
            mechanicArrivedTime = mechanic_arrived_time,
            serviceStartTime = service_start_time,
            serviceEndTime = service_end_time
        )
    }
}

fun ServiceRequest.toDto(): ServiceRequestDto {
    return ServiceRequestDto(
        id = id,
        user_id = userId,
        mechanic_id = mechanicId,
        service_type = serviceType.name,
        status = status.name,
        description = description,
        latitude = latitude,
        longitude = longitude,
        address = address,
        car_model = carModel,
        requested_time = requestedTime,
        estimated_cost = estimatedCost,
        actual_cost = actualCost,
        completed_time = completedTime,
        rating = rating,
        feedback = feedback,
        images = images,
        estimated_duration = estimatedDuration,
        mechanic_accepted_time = mechanicAcceptedTime,
        mechanic_arrived_time = mechanicArrivedTime,
        service_start_time = serviceStartTime,
        service_end_time = serviceEndTime
    )
}