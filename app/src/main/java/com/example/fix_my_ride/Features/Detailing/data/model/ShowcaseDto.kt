package com.example.fix_my_ride.Features.Detailing.data.model

// Features/Detailing/data/model/ShowcaseDto.kt

import com.example.fix_my_ride.Features.Detailing.domain.model.Showcase
import com.google.firebase.firestore.PropertyName

data class ShowcaseDto(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id          : String       = "",

    @get:PropertyName("mechanic_id")
    @set:PropertyName("mechanic_id")
    var mechanicId  : String       = "",

    @get:PropertyName("workshop_id")
    @set:PropertyName("workshop_id")
    var workshopId  : String       = "",

    @get:PropertyName("title")
    @set:PropertyName("title")
    var title       : String       = "",

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description : String       = "",

    @get:PropertyName("image_urls")
    @set:PropertyName("image_urls")
    var imageUrls   : List<String> = emptyList(),

    @get:PropertyName("offer_text")
    @set:PropertyName("offer_text")
    var offerText   : String       = "",

    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    var createdAt   : Long         = 0L
) {
    fun toDomain(): Showcase = Showcase(
        id          = id,
        mechanicId  = mechanicId,
        workshopId  = workshopId,
        title       = title,
        description = description,
        imageUrls   = imageUrls,
        offerText   = offerText,
        createdAt   = createdAt
    )
}