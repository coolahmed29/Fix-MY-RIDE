// Features/Detailing/domain/model/DetailingPackage.kt
package com.example.fix_my_ride.Features.Detailing.domain.model

data class DetailingPackage(
    val id               : String       = "",
    val name             : String       = "",
    val description      : String       = "",
    val price            : Double       = 0.0,
    val type             : PackageType  = PackageType.EXTERIOR,
    val rating           : Float        = 0f,
    val durationHours    : Int          = 1,
    val includedServices : List<String> = emptyList(),  // ✅ = add kiya
    val workshopName     : String       = "",
    val workshopId       : String       = "",
    val thumbnailUrl     : String       = "",
    val beforeAfterUrls  : List<String> = emptyList(),  // ✅ = add kiya
    val isAvailable      : Boolean      = true,
    val createdAt        : Long         = 0L
)