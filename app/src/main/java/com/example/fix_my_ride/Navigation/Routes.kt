package com.example.fix_my_ride.Navigation

import kotlinx.serialization.Serializable

@Serializable data object RegistrationScreen
@Serializable data object LoginScreen

// ── Dashboard ─────────────────────────────────────────
@Serializable data object UserDashboard

// ── Spare Parts ───────────────────────────────────────
@Serializable data object SparePartsScreen
@Serializable data object PartDetailScreen

// ── Detailing ─────────────────────────────────────────
@Serializable data object PackageListScreen
@Serializable data object PackageDetailScreen
@Serializable data object BookingScreen
@Serializable data object BookingConfirmScreen
@Serializable data object ReviewScreen

// ── Chatting ─────────────────────────────────────────
@Serializable
data class ChatScreen(
    val vendorId   : String,
    val vendorName : String
)

// ════════════════════════════════════════════════════════════
// ── MECHANIC FINDER ROUTES ────────────────────────────────
// ════════════════════════════════════════════════════════════

// ✅ Add @Serializable to all mechanic routes
@Serializable
data object MechanicMapScreenRoute

@Serializable
data object MechanicListScreenRoute

@Serializable
data class MechanicProfileScreenRoute(val mechanicId: String)

@Serializable
data class ServiceRequestScreenRoute(val mechanicId: String)

@Serializable
data class LiveTrackingScreenRoute(val requestId: String)