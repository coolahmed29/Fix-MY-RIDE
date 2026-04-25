package com.example.fix_my_ride.Features.SpareParts.Domain.model


data class Part(
    val id          : String = "",
    val name        : String = "",
    val category    : String = "",
    val price       : Double = 0.0,
    val imageUrl    : String = "",
    val rating      : Float  = 0f,
    val description : String = "",
    val carModel    : String = "",
    val vendorCount : Int    = 0,
    val createdAt   : Long   = 0L
)