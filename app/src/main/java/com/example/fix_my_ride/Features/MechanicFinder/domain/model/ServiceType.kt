package com.example.fix_my_ride.Features.MechanicFinder.domain.model


enum class ServiceType(val displayName: String, val icon: String) {
    OIL_CHANGE("Oil Change", "🛢️"),
    TIRE_REPAIR("Tire Repair", "🛞"),
    TIRE_REPLACEMENT("Tire Replacement", "🛞"),
    BATTERY_REPLACEMENT("Battery", "🔋"),
    ENGINE_DIAGNOSTIC("Engine Check", "🔧"),
    BRAKE_SERVICE("Brake Service", "🛑"),
    TRANSMISSION_SERVICE("Transmission", "⚙️"),
    AC_SERVICE("AC Service", "❄️"),
    RADIATOR_FLUSH("Radiator Flush", "💧"),
    SPARK_PLUG_REPLACEMENT("Spark Plugs", "✨"),
    FILTER_REPLACEMENT("Filter Change", "🔄"),
    SUSPENSION_REPAIR("Suspension", "🚗"),
    ELECTRICAL_REPAIR("Electrical", "⚡"),
    PAINT_REPAIR("Paint Repair", "🎨"),
    DENT_REMOVAL("Dent Removal", "🔨"),
    GENERAL_MAINTENANCE("General Maintenance", "🔧"),
    OTHER("Other", "❓");

    companion object {
        fun fromString(value: String): ServiceType {
            return try {
                valueOf(value)
            } catch (e: Exception) {
                OTHER
            }
        }
    }
}