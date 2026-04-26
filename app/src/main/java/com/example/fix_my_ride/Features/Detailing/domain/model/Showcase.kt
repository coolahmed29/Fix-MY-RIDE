package com.example.fix_my_ride.Features.Detailing.domain.model

// Features/Detailing/domain/model/Showcase.kt

data class Showcase(
    val id          : String      = "",
    val mechanicId  : String      = "",
    val workshopId  : String      = "",
    val title       : String      = "",
    val description : String      = "",
    val imageUrls   : List<String> =emptyList(),
val offerText   : String      = "",
val createdAt   : Long        = 0L
)