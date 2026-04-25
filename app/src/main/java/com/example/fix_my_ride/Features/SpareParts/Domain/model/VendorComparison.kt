package com.example.fix_my_ride.Features.SpareParts.Domain.model

// Features/SpareParts/Domain/model/VendorComparison.kt

// Comparison view ke liye
data class VendorComparison(
    val vendors       : List<Vendor> = emptyList(),
    val cheapest      : Vendor?      = null,
    val nearest       : Vendor?      = null,
    val highestRated  : Vendor?      = null,
    val isSingleVendor: Boolean      = false   // sirf 1 vendor hai?
)