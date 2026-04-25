package com.example.fix_my_ride.Features.SpareParts.Data.model

// Features/SpareParts/Data/model/VendorDto.kt

import com.example.fix_my_ride.Features.SpareParts.Domain.model.Vendor
import com.google.firebase.firestore.PropertyName

data class VendorDto(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id        : String  = "",

    @get:PropertyName("part_id")
    @set:PropertyName("part_id")
    var partId    : String  = "",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name      : String  = "",

    @get:PropertyName("price")
    @set:PropertyName("price")
    var price     : Double  = 0.0,

    @get:PropertyName("rating")
    @set:PropertyName("rating")
    var rating    : Float   = 0f,

    @get:PropertyName("distance")
    @set:PropertyName("distance")
    var distance  : Double  = 0.0,

    @get:PropertyName("in_stock")
    @set:PropertyName("in_stock")
    var inStock   : Boolean = true,

    @get:PropertyName("stock_qty")
    @set:PropertyName("stock_qty")
    var stockQty  : Int     = 0,

    @get:PropertyName("phone")
    @set:PropertyName("phone")
    var phone     : String  = "",

    @get:PropertyName("address")
    @set:PropertyName("address")
    var address   : String  = "",

    @get:PropertyName("latitude")
    @set:PropertyName("latitude")
    var latitude  : Double  = 0.0,

    @get:PropertyName("longitude")
    @set:PropertyName("longitude")
    var longitude : Double  = 0.0
) {
    // ── DTO → Domain ──────────────────────────────────
    fun toDomain(): Vendor = Vendor(
        id        = id,
        partId    = partId,
        name      = name,
        price     = price,
        rating    = rating,
        distance  = distance,
        inStock   = inStock,
        stockQty  = stockQty,
        phone     = phone,
        address   = address,
        latitude  = latitude,
        longitude = longitude
    )

    companion object {
        // ── Domain → DTO ──────────────────────────────
        fun fromDomain(vendor: Vendor): VendorDto = VendorDto(
            id        = vendor.id,
            partId    = vendor.partId,
            name      = vendor.name,
            price     = vendor.price,
            rating    = vendor.rating,
            distance  = vendor.distance,
            inStock   = vendor.inStock,
            stockQty  = vendor.stockQty,
            phone     = vendor.phone,
            address   = vendor.address,
            latitude  = vendor.latitude,
            longitude = vendor.longitude
        )
    }
}