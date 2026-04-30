package com.example.fix_my_ride.Features.SpareParts.Domain.model


// Domain/model/Order.kt

data class Order(
    val id            : String  = "",
    val partId        : String  = "",
    val partName      : String  = "",
    val vendorId      : String  = "",
    val vendorName    : String  = "",
    val vendorAddress : String  = "",
    val quantity      : Int     = 1,
    val pricePerUnit  : Double  = 0.0,
    val totalPrice    : Double  = 0.0,
    val buyerName     : String  = "",
    val buyerPhone    : String  = "",
    val buyerAddress  : String  = "",
    val paymentMethod : PaymentMethod = PaymentMethod.CASH_ON_DELIVERY,
    val status        : OrderStatus   = OrderStatus.PENDING,
    val placedAt      : Long    = System.currentTimeMillis()
)

enum class PaymentMethod(val label: String) {
    CASH_ON_DELIVERY("Cash on Delivery"),
    CARD("Credit / Debit Card"),
    JAZZ_CASH("JazzCash"),
    EASY_PAISA("EasyPaisa")
}

enum class OrderStatus(val label: String) {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}