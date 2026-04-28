package com.example.fix_my_ride.Features.MechanicFinder.data.model


import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.MechanicReview
import com.google.firebase.firestore.PropertyName

data class MechanicDto(
    @PropertyName("id")
    val id: String = "",

    @PropertyName("name")
    val name: String = "",

    @PropertyName("phone")
    val phone: String = "",

    @PropertyName("email")
    val email: String = "",

    @PropertyName("rating")
    val rating: Double = 0.0,

    @PropertyName("experience")
    val experience: Int = 0,

    @PropertyName("latitude")
    val latitude: Double = 0.0,

    @PropertyName("longitude")
    val longitude: Double = 0.0,

    @PropertyName("distance")
    val distance: Double = 0.0,

    @PropertyName("is_available")
    val isAvailable: Boolean = true,

    @PropertyName("specializations")
    val specializations: List<String> = emptyList(),

    @PropertyName("average_response_time")
    val averageResponseTime: Int = 0,

    @PropertyName("completed_jobs")
    val completedJobs: Int = 0,

    @PropertyName("profile_image_url")
    val profileImageUrl: String? = null,

    @PropertyName("workshop_name")
    val workshopName: String? = null,

    @PropertyName("workshop_address")
    val workshopAddress: String? = null,

    @PropertyName("hourly_rate")
    val hourlyRate: Double = 0.0,

    @PropertyName("reviews")
    val reviews: List<MechanicReviewDto> = emptyList()
) {
    fun toDomainModel(): Mechanic {
        return Mechanic(
            id = id,
            name = name,
            phone = phone,
            email = email,
            rating = rating,
            experience = experience,
            latitude = latitude,
            longitude = longitude,
            distance = distance,
            isAvailable = isAvailable,
            specializations = specializations,
            averageResponseTime = averageResponseTime,
            completedJobs = completedJobs,
            profileImageUrl = profileImageUrl,
            workshopName = workshopName,
            workshopAddress = workshopAddress,
            hourlyRate = hourlyRate,
            reviews = reviews.map { it.toDomainModel() }
        )
    }
}

data class MechanicReviewDto(
    @PropertyName("id")
    val id: String = "",

    @PropertyName("user_id")
    val userId: String = "",

    @PropertyName("user_name")
    val userName: String = "",

    @PropertyName("rating")
    val rating: Double = 0.0,

    @PropertyName("comment")
    val comment: String = "",

    @PropertyName("timestamp")
    val timestamp: Long = 0L
) {
    fun toDomainModel(): MechanicReview {
        return MechanicReview(
            id = id,
            userId = userId,
            userName = userName,
            rating = rating,
            comment = comment,
            timestamp = timestamp
        )
    }
}