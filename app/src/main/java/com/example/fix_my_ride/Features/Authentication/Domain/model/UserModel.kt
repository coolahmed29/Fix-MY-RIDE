package com.example.fix_my_ride.Features.Authentication.Domain.model


data class UserModel(
    val uid       : String = "",
    val name      : String = "",
    val phone     : String = "",
    val email     : String = "",
    val token     : String = "",
    val createdAt : Long   = System.currentTimeMillis()
)

