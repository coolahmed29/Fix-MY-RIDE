package com.example.fix_my_ride.Features.SpareParts.Data.model

// Features/SpareParts/Data/model/PartDto.kt
// Features/SpareParts/Data/model/PartDto.kt

import com.example.fix_my_ride.Features.SpareParts.Domain.model.Part
import com.google.firebase.firestore.PropertyName

data class PartDto(
    // Firestore fields — exactly same naam hone chahiye
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id          : String = "",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name        : String = "",

    @get:PropertyName("category")
    @set:PropertyName("category")
    var category    : String = "",

    @get:PropertyName("price")
    @set:PropertyName("price")
    var price       : Double = 0.0,

    @get:PropertyName("image_url")       // Firestore mein snake_case
    @set:PropertyName("image_url")
    var imageUrl    : String = "",

    @get:PropertyName("rating")
    @set:PropertyName("rating")
    var rating      : Float  = 0f,

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description : String = "",

    @get:PropertyName("car_model")
    @set:PropertyName("car_model")
    var carModel    : String = "",

    @get:PropertyName("vendor_count")
    @set:PropertyName("vendor_count")
    var vendorCount : Int    = 0,

    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    var createdAt   : Long   = 0L
) {
    // ── DTO → Domain Model ────────────────────────────
    fun toDomain(): Part = Part(
        id          = id,
        name        = name,
        category    = category,
        price       = price,
        imageUrl    = imageUrl,
        rating      = rating,
        description = description,
        carModel    = carModel,
        vendorCount = vendorCount,
        createdAt   = createdAt
    )

    companion object {
        // ── Domain Model → DTO (Firestore mein save ke liye) ──
        fun fromDomain(part: Part): PartDto = PartDto(
            id          = part.id,
            name        = part.name,
            category    = part.category,
            price       = part.price,
            imageUrl    = part.imageUrl,
            rating      = part.rating,
            description = part.description,
            carModel    = part.carModel,
            vendorCount = part.vendorCount,
            createdAt   = part.createdAt
        )
    }
}