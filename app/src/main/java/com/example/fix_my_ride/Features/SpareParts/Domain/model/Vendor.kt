package com.example.fix_my_ride.Features.SpareParts.Domain.model

// Features/SpareParts/Domain/model/Vendor.kt

data class Vendor(
    val id        : String  = "",
    val partId    : String  = "",
    val name      : String  = "",
    val price     : Double  = 0.0,
    val rating    : Float   = 0f,
    val distance  : Double  = 0.0,   // KM mein
    val inStock   : Boolean = true,
    val stockQty  : Int     = 0,
    val phone     : String  = "",
    val address   : String  = "",
    val latitude  : Double  = 0.0,
    val longitude : Double  = 0.0
)