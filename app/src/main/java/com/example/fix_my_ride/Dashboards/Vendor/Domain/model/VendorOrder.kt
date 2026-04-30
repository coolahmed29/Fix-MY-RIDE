package com.example.fix_my_ride.Dashboards.Vendor.Domain.model

data class VendorOrder(
    val id           : String      = "",
    val customerName : String      = "",
    val partName     : String      = "",
    val quantity     : Int         = 1,
    val totalPrice   : Double      = 0.0,
    val date         : String      = "",
    val status       : OrderStatus = OrderStatus.PENDING
)

enum class OrderStatus { PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED }