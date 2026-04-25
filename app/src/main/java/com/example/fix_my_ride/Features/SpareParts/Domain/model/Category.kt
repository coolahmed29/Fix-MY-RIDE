package com.example.fix_my_ride.Features.SpareParts.Domain.model

// Features/SpareParts/Domain/model/Category.kt

// Note: Icon aur Color Domain mein nahi hote
// Yeh sirf pure data hai
data class Category(
    val id   : String = "",
    val name : String = "",
    val slug : String = ""  // firestore mein search key
)