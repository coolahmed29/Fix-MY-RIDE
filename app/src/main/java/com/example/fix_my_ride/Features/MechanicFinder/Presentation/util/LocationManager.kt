package com.example.fix_my_ride.Features.MechanicFinder.Presentation.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager as AndroidLocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import android.util.Log

class LocationManager @Inject constructor(
    private val context: Context
) {
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    suspend fun getCurrentLocation(): Pair<Double, Double>? = suspendCancellableCoroutine { continuation ->
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("LocationManager", "No location permission")
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }

        try {
            fusedLocationProviderClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            )
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        Log.d("LocationManager", "Got location: ${location.latitude}, ${location.longitude}")
                        continuation.resume(Pair(location.latitude, location.longitude))
                    } else {
                        Log.d("LocationManager", "Location is null, using mock location")
                        // ✅ Fallback to mock location (Karachi)
                        continuation.resume(Pair(24.8607, 67.0011))
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("LocationManager", "Location error: ${exception.message}")
                    // ✅ Fallback to mock location on error
                    continuation.resume(Pair(24.8607, 67.0011))
                }
        } catch (e: Exception) {
            Log.e("LocationManager", "Exception: ${e.message}")
            continuation.resume(Pair(24.8607, 67.0011))
        }
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}