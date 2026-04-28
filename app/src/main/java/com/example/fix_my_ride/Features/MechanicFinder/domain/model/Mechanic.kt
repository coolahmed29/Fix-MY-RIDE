package com.example.fix_my_ride.Features.MechanicFinder.domain.model


data class Mechanic(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val rating: Double,
    val experience: Int,  // years
    val latitude: Double,
    val longitude: Double,
    val distance: Double,  // km
    val isAvailable: Boolean,
    val specializations: List<String>,
    val averageResponseTime: Int,  // minutes
    val completedJobs: Int,
    val profileImageUrl: String?,
    val workshopName: String?,
    val workshopAddress: String?,
    val hourlyRate: Double,
    val reviews: List<MechanicReview> = emptyList()
)

data class MechanicReview(
    val id: String,
    val userId: String,
    val userName: String,
    val rating: Double,
    val comment: String,
    val timestamp: Long
)