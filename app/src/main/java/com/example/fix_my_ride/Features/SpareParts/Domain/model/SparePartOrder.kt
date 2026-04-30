package com.example.fix_my_ride.Features.SpareParts.Domain.model

data class SparePartOrder(
    val orderId: String = "",
    val userId: String = "",
    val vendorId: String = "",
    val vendorName: String = "",
    val partName: String = "",
    val amount: Double = 0.0,
    val status: String = "Pending", // Pending, Confirmed, Delivered
    val timestamp: Long = System.currentTimeMillis()
)