package com.example.fix_my_ride.Dashboards.Workshop.Domain.model

data class WorkshopRequest(
    val id          : String         = "",
    val customerName: String         = "",
    val packageName : String         = "",
    val date        : String         = "",
    val timeSlot    : String         = "",
    val price       : Double         = 0.0,
    val status      : RequestStatus  = RequestStatus.PENDING
)

enum class RequestStatus { PENDING, ACCEPTED, REJECTED }