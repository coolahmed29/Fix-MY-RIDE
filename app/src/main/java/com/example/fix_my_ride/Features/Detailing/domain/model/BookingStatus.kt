package com.example.fix_my_ride.Features.Detailing.domain.model

// Features/Detailing/domain/model/BookingStatus.kt

enum class BookingStatus(val displayName: String) {
    PENDING     ("Pending"),
    CONFIRMED   ("Confirmed"),
    IN_PROGRESS ("In Progress"),
    COMPLETED   ("Completed"),
    CANCELLED   ("Cancelled")
}