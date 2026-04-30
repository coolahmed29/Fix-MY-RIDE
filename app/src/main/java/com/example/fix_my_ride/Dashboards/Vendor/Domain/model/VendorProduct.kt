package com.example.fix_my_ride.Dashboards.Vendor.Domain.model

data class VendorProduct(
    val id          : String  = "",
    val name        : String  = "",
    val category    : String  = "",
    val price       : Double  = 0.0,
    val stock       : Int     = 0,
    val isAvailable : Boolean = true
)