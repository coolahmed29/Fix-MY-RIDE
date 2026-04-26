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