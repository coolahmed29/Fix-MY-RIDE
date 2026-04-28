package com.example.fix_my_ride.Features.MechanicFinder.Presentation.view

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic
import com.example.fix_my_ride.Features.MechanicFinder.Presentation.viewmodel.MechanicFinderViewModel
import com.example.fix_my_ride.Features.MechanicFinder.Presentation.util.LocationManager
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.ui.theme.DashBackground
import com.example.fix_my_ride.ui.theme.DashCardBg
import com.example.fix_my_ride.ui.theme.DashTextPrimary
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import android.util.Log

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MechanicMapScreen(
    onMechanicClick: (mechanicId: String, mechanicName: String) -> Unit,
    onBack: () -> Unit,
    viewModel: MechanicFinderViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val locationManager = remember { LocationManager(context) }

    var userLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var isLoadingLocation by remember { mutableStateOf(true) }
    var hasPermission by remember { mutableStateOf(false) }

    val mechanicsState by viewModel.nearbyMechanicsState
        .collectAsStateWithLifecycle()

    // ✅ Request Location Permissions
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(permissionState.allPermissionsGranted) {
        hasPermission = permissionState.allPermissionsGranted

        if (permissionState.allPermissionsGranted) {
            Log.d("MechanicMap", "Permission granted, getting location...")
            val location = locationManager.getCurrentLocation()
            Log.d("MechanicMap", "Location: $location")

            userLocation = location ?: Pair(24.8607, 67.0011) // Fallback to Karachi
            isLoadingLocation = false

            if (userLocation != null) {
                viewModel.fetchNearbyMechanics(
                    latitude = userLocation!!.first,
                    longitude = userLocation!!.second,
                    radiusKm = 10.0
                )
            }
        } else {
            isLoadingLocation = false
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            Log.d("MechanicMap", "Requesting permissions...")
            permissionState.launchMultiplePermissionRequest()
        }
    }

    Scaffold(
        containerColor = DashBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ✅ Show Map if location available
            if (hasPermission && userLocation != null && !isLoadingLocation) {
                MechanicMapView(
                    userLocation = userLocation!!,
                    mechanics = (mechanicsState as? AuthResult.Success<*>)?.data as? List<Mechanic> ?: emptyList(),
                    onMechanicClick = onMechanicClick,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (isLoadingLocation) {
                // Loading state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DashBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Primary, modifier = Modifier.size(50.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Getting your location...",
                            fontFamily = Roboto,
                            fontSize = 14.sp,
                            color = DashTextPrimary
                        )
                    }
                }
            } else if (!hasPermission) {
                // Permission denied state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DashBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterVertically as Alignment.Horizontal,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Location Permission Required",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DashTextPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "We need your location to find nearby mechanics",
                            fontFamily = Roboto,
                            fontSize = 13.sp,
                            color = DashTextSecondary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            // Mechanics List Overlay
            if (hasPermission && userLocation != null) {
                when (mechanicsState) {
                    is AuthResult.Loading -> {
                        // Show in overlay
                    }

                    is AuthResult.Success<*> -> {
                        @Suppress("UNCHECKED_CAST")
                        val mechanics = (mechanicsState as? AuthResult.Success<*>)?.data as? List<Mechanic> ?: emptyList()

                        if (mechanics.isNotEmpty()) {
                            MechanicsListOverlay(
                                mechanics = mechanics,
                                onMechanicClick = onMechanicClick,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                            )
                        }
                    }

                    is AuthResult.Error -> {
                        Log.e("MechanicMap", "Error: ${(mechanicsState as AuthResult.Error).message}")
                    }

                    else -> {}
                }
            }

            // Header
            MechanicMapHeader(onBack = onBack)
        }
    }
}

// ── Google Map View ──────────────────────────────────
@Composable
private fun MechanicMapView(
    userLocation: Pair<Double, Double>,
    mechanics: List<Mechanic>,
    onMechanicClick: (mechanicId: String, mechanicName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val userLatLng = LatLng(userLocation.first, userLocation.second)

    Log.d("MechanicMapView", "Rendering map with location: $userLatLng")

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.Builder()
            .target(userLatLng)
            .zoom(14f)
            .build()
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = MapType.NORMAL
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            compassEnabled = true,
            myLocationButtonEnabled = true
        )
    ) {
        // ✅ User Location Marker
        Marker(
            state = MarkerState(position = userLatLng),
            title = "Your Location",
            snippet = "You are here"
        )

        // ✅ Mechanic Markers
        mechanics.forEach { mechanic ->
            val mechanicLatLng = LatLng(mechanic.latitude, mechanic.longitude)
            Marker(
                state = MarkerState(position = mechanicLatLng),
                title = mechanic.name,
                snippet = "⭐ ${String.format("%.1f", mechanic.rating)} • ${String.format("%.1f", mechanic.distance)}km",
                onClick = {
                    Log.d("MechanicMap", "Clicked on: ${mechanic.name}")
                    onMechanicClick(mechanic.id, mechanic.name)
                    true
                }
            )
        }
    }
}

@Composable
private fun MechanicMapHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(DashCardBg),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = DashTextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Text(
            text = "Find Mechanics",
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = DashTextPrimary
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(DashCardBg),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filter",
                    tint = DashTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun MechanicsListOverlay(
    mechanics: List<Mechanic>,
    onMechanicClick: (mechanicId: String, mechanicName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(DashCardBg)
            .padding(16.dp)
            .height(300.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nearby Mechanics",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DashTextPrimary
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Primary.copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${mechanics.size} found",
                    fontFamily = Roboto,
                    fontSize = 12.sp,
                    color = Primary
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(mechanics) { mechanic ->
                MechanicRowCard(
                    mechanic = mechanic,
                    onClick = { onMechanicClick(mechanic.id, mechanic.name) }
                )
            }
        }
    }
}

@Composable
private fun MechanicRowCard(
    mechanic: Mechanic,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DashBackground)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mechanic.name,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DashTextPrimary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⭐ ${String.format("%.1f", mechanic.rating)}",
                        fontFamily = Roboto,
                        fontSize = 12.sp,
                        color = DashTextSecondary
                    )
                    Text(
                        text = " • ${String.format("%.1f", mechanic.distance)}km",
                        fontFamily = Roboto,
                        fontSize = 12.sp,
                        color = DashTextSecondary
                    )
                }
            }

            if (mechanic.isAvailable) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Primary.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "✓ Available",
                        fontFamily = Roboto,
                        fontSize = 11.sp,
                        color = Primary
                    )
                }
            }
        }
    }
}