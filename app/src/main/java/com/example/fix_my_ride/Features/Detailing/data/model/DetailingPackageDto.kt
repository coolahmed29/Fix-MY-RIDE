package com.example.fix_my_ride.Features.Detailing.data.model

// Features/Detailing/data/model/DetailingPackageDto.kt
import com.example.fix_my_ride.Features.Detailing.domain.model.DetailingPackage
import com.example.fix_my_ride.Features.Detailing.domain.model.PackageType
import com.google.firebase.firestore.PropertyName

data class DetailingPackageDto(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id               : String       = "",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name             : String       = "",

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description      : String       = "",

    @get:PropertyName("price")
    @set:PropertyName("price")
    var price            : Double       = 0.0,

    @get:PropertyName("type")
    @set:PropertyName("type")
    var type             : String       = PackageType.EXTERIOR.name,

    @get:PropertyName("rating")
    @set:PropertyName("rating")
    var rating           : Float        = 0f,

    @get:PropertyName("duration_hours")
    @set:PropertyName("duration_hours")
    var durationHours    : Int          = 1,

    @get:PropertyName("included_services")
    @set:PropertyName("included_services")
    var includedServices : List<String> = emptyList(),

    @get:PropertyName("workshop_name")
    @set:PropertyName("workshop_name")
    var workshopName     : String       = "",

    @get:PropertyName("workshop_id")
    @set:PropertyName("workshop_id")
    var workshopId       : String       = "",

    @get:PropertyName("thumbnail_url")
    @set:PropertyName("thumbnail_url")
    var thumbnailUrl     : String       = "",

    @get:PropertyName("before_after_urls")
    @set:PropertyName("before_after_urls")
    var beforeAfterUrls  : List<String> = emptyList(),

    @get:PropertyName("is_available")
    @set:PropertyName("is_available")
    var isAvailable      : Boolean      = true,

    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    var createdAt        : Long         = 0L
) {
    fun toDomain(): DetailingPackage = DetailingPackage(
        id               = id,
        name             = name,
        description      = description,
        price            = price,
        type             = runCatching {
            PackageType.valueOf(type)
        }.getOrDefault(PackageType.EXTERIOR),
        rating           = rating,
        durationHours    = durationHours,
        includedServices = includedServices,
        workshopName     = workshopName,
        workshopId       = workshopId,
        thumbnailUrl     = thumbnailUrl,
        beforeAfterUrls  = beforeAfterUrls,
        isAvailable      = isAvailable,
        createdAt        = createdAt
    )
}