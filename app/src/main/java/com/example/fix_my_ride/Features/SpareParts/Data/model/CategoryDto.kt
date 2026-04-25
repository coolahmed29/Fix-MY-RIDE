package com.example.fix_my_ride.Features.SpareParts.Data.model

// Features/SpareParts/Data/model/CategoryDto.kt

import com.example.fix_my_ride.Features.SpareParts.Domain.model.Category
import com.google.firebase.firestore.PropertyName

data class CategoryDto(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id   : String = "",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name : String = "",

    @get:PropertyName("slug")
    @set:PropertyName("slug")
    var slug : String = ""
) {
    fun toDomain(): Category = Category(
        id   = id,
        name = name,
        slug = slug
    )
}