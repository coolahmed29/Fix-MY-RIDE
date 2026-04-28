package com.example.fix_my_ride.Features.MechanicFinder.Presentation.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceRequest
import com.example.fix_my_ride.Features.MechanicFinder.Presentation.viewmodel.MechanicFinderViewModel
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.ui.theme.DashBackground
import com.example.fix_my_ride.ui.theme.DashCardBg
import com.example.fix_my_ride.ui.theme.DashTextLight
import com.example.fix_my_ride.ui.theme.DashTextPrimary
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LiveTrackingScreen(
    serviceRequest: ServiceRequest?,
    mechanic: Mechanic?,
    onBack: () -> Unit,
    onChatClick: (vendorId: String, vendorName: String) -> Unit,  // ✅ Chat lambda
    viewModel: MechanicFinderViewModel = hiltViewModel()
) {
    if (serviceRequest == null || mechanic == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Primary)
        }
        return
    }

    // Start tracking mechanic location
    LaunchedEffect(mechanic.id) {
        viewModel.trackMechanicLocation(mechanic.id)
    }

    val mechanicLocation by viewModel.mechanicLocationState
        .collectAsStateWithLifecycle()

    var currentLatitude by remember { mutableStateOf(mechanic.latitude) }
    var currentLongitude by remember { mutableStateOf(mechanic.longitude) }
    var distanceToMechanic by remember { mutableStateOf(calculateDistance(mechanic.latitude, mechanic.longitude)) }

    // Update location when received from flow
    if (mechanicLocation is AuthResult.Success<*>) {
        val location = (mechanicLocation as AuthResult.Success<*>).data as? Pair<Double, Double>
        if (location != null) {
            currentLatitude = location.first
            currentLongitude = location.second
            distanceToMechanic = calculateDistance(location.first, location.second)
        }
    }

    Scaffold(
        containerColor = DashBackground
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            item(key = "header") {
                TrackingHeader(onBack = onBack)
            }

            item(key = "map_placeholder") {
                MapPlaceholder(
                    currentLatitude = currentLatitude,
                    currentLongitude = currentLongitude,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item(key = "distance_info") {
                DistanceInfoCard(
                    distanceToMechanic = distanceToMechanic,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item(key = "mechanic_info") {
                MechanicTrackingCard(
                    mechanic = mechanic,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item(key = "status_timeline") {
                StatusTimelineSection(
                    serviceRequest = serviceRequest,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item(key = "action_buttons") {
                TrackingActionButtons(
                    mechanic = mechanic,
                    onChatClick = onChatClick,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
        }
    }
}

// ── Header ───────────────────────────────────────��────
@Composable
private fun TrackingHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
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

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "Live Tracking",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DashTextPrimary
            )
            Text(
                text = "Mechanic on the way",
                fontFamily = Roboto,
                fontSize = 12.sp,
                color = DashTextSecondary
            )
        }
    }
}

// ── Map Placeholder ───────────────────────────────────
@Composable
private fun MapPlaceholder(
    currentLatitude: Double,
    currentLongitude: Double,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(alpha = 0.04f))
            .clip(RoundedCornerShape(16.dp))
            .background(DashCardBg)
    ) {
        // TODO: Integrate Google Maps here
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "📍 Live Map View",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DashTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Lat: ${"%.4f".format(currentLatitude)}, Lon: ${"%.4f".format(currentLongitude)}",
                fontFamily = Roboto,
                fontSize = 11.sp,
                color = DashTextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Google Maps integration coming soon",
                fontFamily = Roboto,
                fontSize = 12.sp,
                color = DashTextLight
            )
        }
    }
}

// ── Distance Info Card ────────────────────────────────
@Composable
private fun DistanceInfoCard(
    distanceToMechanic: Double,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(alpha = 0.04f))
            .clip(RoundedCornerShape(16.dp))
            .background(Primary.copy(alpha = 0.08f))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${"%.1f".format(distanceToMechanic)} km",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Primary
                )
                Text(
                    text = "Away",
                    fontFamily = Roboto,
                    fontSize = 11.sp,
                    color = DashTextSecondary
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .height(60.dp)
                    .width(1.dp),
                color = Primary.copy(alpha = 0.3f)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Live",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50)
                )
                Text(
                    text = "Tracking",
                    fontFamily = Roboto,
                    fontSize = 11.sp,
                    color = DashTextSecondary
                )
            }
        }
    }
}

// ── Mechanic Tracking Card ────────────────────────────
@Composable
private fun MechanicTrackingCard(
    mechanic: Mechanic,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(alpha = 0.04f))
            .clip(RoundedCornerShape(16.dp))
            .background(DashCardBg)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mechanic.name.first().toString(),
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Primary
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mechanic.name,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
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
                        text = " • 🚗 On the way",
                        fontFamily = Roboto,
                        fontSize = 12.sp,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
            )
        }
    }
}

// ── Status Timeline Section ───────────────────────────
@Composable
private fun StatusTimelineSection(
    serviceRequest: ServiceRequest,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Service Status",
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = DashTextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DashCardBg)
                .padding(12.dp)
        ) {
            Column {
                TimelineItem(
                    icon = "📞",
                    title = "Request Sent",
                    time = formatTime(serviceRequest.requestedTime),
                    isCompleted = true,
                    isCurrent = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                TimelineItem(
                    icon = "✓",
                    title = "Mechanic Accepted",
                    time = if (serviceRequest.mechanicAcceptedTime != null)
                        formatTime(serviceRequest.mechanicAcceptedTime) else "Waiting...",
                    isCompleted = serviceRequest.mechanicAcceptedTime != null,
                    isCurrent = serviceRequest.mechanicAcceptedTime != null
                )

                Spacer(modifier = Modifier.height(12.dp))

                TimelineItem(
                    icon = "🚗",
                    title = "On the Way",
                    time = if (serviceRequest.mechanicArrivedTime != null)
                        formatTime(serviceRequest.mechanicArrivedTime) else "In progress...",
                    isCompleted = serviceRequest.mechanicArrivedTime != null,
                    isCurrent = serviceRequest.mechanicArrivedTime == null &&
                            serviceRequest.mechanicAcceptedTime != null
                )

                Spacer(modifier = Modifier.height(12.dp))

                TimelineItem(
                    icon = "🔧",
                    title = "Service Started",
                    time = if (serviceRequest.serviceStartTime != null)
                        formatTime(serviceRequest.serviceStartTime) else "Pending...",
                    isCompleted = serviceRequest.serviceStartTime != null,
                    isCurrent = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                TimelineItem(
                    icon = "✅",
                    title = "Service Completed",
                    time = if (serviceRequest.serviceEndTime != null)
                        formatTime(serviceRequest.serviceEndTime) else "Pending...",
                    isCompleted = serviceRequest.serviceEndTime != null,
                    isCurrent = false
                )
            }
        }
    }
}

@Composable
private fun TimelineItem(
    icon: String,
    title: String,
    time: String,
    isCompleted: Boolean,
    isCurrent: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCompleted -> Primary
                        isCurrent -> Color(0xFF4CAF50)
                        else -> DashBackground
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = DashTextPrimary
            )
            Text(
                text = time,
                fontFamily = Roboto,
                fontSize = 11.sp,
                color = DashTextSecondary
            )
        }
    }
}

// ── Action Buttons ────────────────────────────────────
@Composable
private fun TrackingActionButtons(
    mechanic: Mechanic,
    onChatClick: (vendorId: String, vendorName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(
            onClick = { onChatClick(mechanic.id, mechanic.name) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Chat with Mechanic",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { /* TODO: Call mechanic */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DashCardBg),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Call Mechanic: ${mechanic.phone}",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { /* TODO: Cancel request */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Cancel Request",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFFC62828)
            )
        }
    }
}

// ── Helper Functions ──────────────────────────────────

fun calculateDistance(latitude: Double, longitude: Double): Double {
    // TODO: Replace with user's actual location
    val userLatitude = 24.8607
    val userLongitude = 67.0011

    val r = 6371 // Earth's radius in km
    val dLat = Math.toRadians(latitude - userLatitude)
    val dLon = Math.toRadians(longitude - userLongitude)
    val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
            kotlin.math.cos(Math.toRadians(userLatitude)) * kotlin.math.cos(Math.toRadians(latitude)) *
            kotlin.math.sin(dLon / 2) * kotlin.math.sin(dLon / 2)
    val c = 2 * kotlin.math.asin(kotlin.math.sqrt(a))
    return r * c
}

fun formatTime(timestamp: Long?): String {
    if (timestamp == null) return "Pending..."
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}