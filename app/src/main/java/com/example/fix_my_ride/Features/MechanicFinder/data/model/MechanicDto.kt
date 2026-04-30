package com.example.fix_my_ride.Features.MechanicFinder.data.model

import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.MechanicReview
import com.google.firebase.firestore.PropertyName

data class MechanicDto(
    @get:PropertyName("id") @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("name") @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("phone") @set:PropertyName("phone")
    var phone: String = "",

    @get:PropertyName("email") @set:PropertyName("email")
    var email: String = "",

    @get:PropertyName("rating") @set:PropertyName("rating")
    var rating: Double = 0.0,

    @get:PropertyName("experience") @set:PropertyName("experience")
    var experience: Int = 0,

    @get:PropertyName("latitude") @set:PropertyName("latitude")
    var latitude: Double? = null,

    @get:PropertyName("longitude") @set:PropertyName("longitude")
    var longitude: Double? = null,

    @get:PropertyName("distance") @set:PropertyName("distance")
    var distance: Double = 0.0,

    @get:PropertyName("is_available") @set:PropertyName("is_available")
    var is_available: Boolean = true,

    @get:PropertyName("specializations") @set:PropertyName("specializations")
    var specializations: List<String> = emptyList(),

    @get:PropertyName("average_response_time") @set:PropertyName("average_response_time")
    var average_response_time: Int = 0,

    @get:PropertyName("completed_jobs") @set:PropertyName("completed_jobs")
    var completed_jobs: Int = 0,

    @get:PropertyName("profile_image_url") @set:PropertyName("profile_image_url")
    var profile_image_url: String? = null,

    @get:PropertyName("workshop_name") @set:PropertyName("workshop_name")
    var workshop_name: String? = null,

    @get:PropertyName("workshop_address") @set:PropertyName("workshop_address")
    var workshop_address: String? = null,

    @get:PropertyName("hourly_rate") @set:PropertyName("hourly_rate")
    var hourly_rate: Double = 0.0,

    @get:PropertyName("reviews") @set:PropertyName("reviews")
    var reviews: List<MechanicReviewDto> = emptyList()
) {
    // Firestore ke liye no-arg constructor
    constructor() : this(id = "")

    fun toDomainModel(): Mechanic {
        return Mechanic(
            id = id,
            name = name,
            phone = phone,
            email = email,
            rating = rating,
            experience = experience,
            latitude = latitude ?: 0.0,
            longitude = longitude ?: 0.0,
            distance = distance,
            isAvailable = is_available,
            specializations = specializations,
            averageResponseTime = average_response_time,
            completedJobs = completed_jobs,
            profileImageUrl = profile_image_url,
            workshopName = workshop_name,
            workshopAddress = workshop_address,
            hourlyRate = hourly_rate,
            reviews = reviews.map { it.toDomainModel() }
        )
    }
}

data class MechanicReviewDto(
    @get:PropertyName("id") @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("user_id") @set:PropertyName("user_id")
    var user_id: String = "",

    @get:PropertyName("user_name") @set:PropertyName("user_name")
    var user_name: String = "",

    @get:PropertyName("rating") @set:PropertyName("rating")
    var rating: Double = 0.0,

    @get:PropertyName("comment") @set:PropertyName("comment")
    var comment: String = "",

    @get:PropertyName("timestamp") @set:PropertyName("timestamp")
    var timestamp: Long = 0L
) {
    constructor() : this(id = "")

    fun toDomainModel(): MechanicReview {
        return MechanicReview(
            id = id,
            userId = user_id,
            userName = user_name,
            rating = rating,
            comment = comment,
            timestamp = timestamp
        )
    }
}