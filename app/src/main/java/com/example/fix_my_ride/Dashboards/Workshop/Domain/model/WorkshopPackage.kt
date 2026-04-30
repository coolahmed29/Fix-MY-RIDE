package com.example.fix_my_ride.Dashboards.Workshop.Domain.model

data class WorkshopPackage(
    val id          : String  = "",
    val name        : String  = "",
    val description : String  = "",
    val price       : Double  = 0.0,
    val duration    : String  = "",   // e.g. "2-3 hours"
    val isAvailable : Boolean = true
)
