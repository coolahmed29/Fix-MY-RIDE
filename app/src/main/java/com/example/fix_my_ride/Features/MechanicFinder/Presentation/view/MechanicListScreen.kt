package com.example.fix_my_ride.Features.MechanicFinder.Presentation.view


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.fix_my_ride.Features.MechanicFinder.Presentation.viewmodel.MechanicFinderViewModel
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.ui.Components.EmptyState
import com.example.fix_my_ride.ui.theme.DashBackground
import com.example.fix_my_ride.ui.theme.DashCardBg
import com.example.fix_my_ride.ui.theme.DashTextLight
import com.example.fix_my_ride.ui.theme.DashTextPrimary
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
fun MechanicListScreen(
    onMechanicClick: (mechanicId: String, mechanicName: String) -> Unit,  // ✅ lambda
    onBack: () -> Unit,
    viewModel: MechanicFinderViewModel = hiltViewModel()
) {
    val mechanicsState by viewModel.nearbyMechanicsState
        .collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchNearbyMechanics(
            latitude = 24.8607,
            longitude = 67.0011,
            radiusKm = 10.0
        )
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
                MechanicListHeader(onBack = onBack)
            }

            when (mechanicsState) {
                is AuthResult.Loading -> {
                    item(key = "loading") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Primary, modifier = Modifier.size(40.dp))
                        }
                    }
                }

                is AuthResult.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    val mechanics = (mechanicsState as? AuthResult.Success<*>)?.data as? List<Mechanic> ?: emptyList()

                    if (mechanics.isEmpty()) {
                        item(key = "empty") {
                            EmptyState(
                                icon = Icons.Default.Star,
                                title = "No Mechanics Available",
                                subtitle = "Koi mechanic nahi mil raha ابھی"
                            )
                        }
                    } else {
                        items(items = mechanics, key = { it.id }) { mechanic ->
                            MechanicCard(
                                mechanic = mechanic,
                                onClick = { onMechanicClick(mechanic.id, mechanic.name) },  // ✅ lambda call
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                is AuthResult.Error -> {
                    item(key = "error") {
                        EmptyState(
                            icon = Icons.Default.Star,
                            title = "Error Loading Mechanics",
                            subtitle = (mechanicsState as AuthResult.Error).message
                        )
                    }
                }

                null -> {
                    item(key = "null") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MechanicListHeader(onBack: () -> Unit) {
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
                text = "Available Mechanics",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DashTextPrimary
            )
            Text(
                text = "Book a mechanic nearby",
                fontFamily = Roboto,
                fontSize = 12.sp,
                color = DashTextSecondary
            )
        }
    }
}

@Composable
private fun MechanicCard(
    mechanic: Mechanic,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(alpha = 0.04f))
            .clip(RoundedCornerShape(16.dp))
            .background(DashCardBg)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
                        fontSize = 24.sp,
                        color = Primary
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = mechanic.name,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DashTextPrimary
                    )
                    Text(
                        text = mechanic.workshopName ?: "Workshop",
                        fontFamily = Roboto,
                        fontSize = 12.sp,
                        color = DashTextSecondary
                    )
                }

                if (mechanic.isAvailable) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50))
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color.Black.copy(alpha = 0.05f))
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MechanicStatItem(label = "Rating", value = "⭐ ${String.format("%.1f", mechanic.rating)}")
                MechanicStatItem(label = "Experience", value = "${mechanic.experience} yrs")
                MechanicStatItem(label = "Distance", value = "${String.format("%.1f", mechanic.distance)}km")
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color.Black.copy(alpha = 0.05f))
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hourly Rate",
                        fontFamily = Roboto,
                        fontSize = 11.sp,
                        color = DashTextLight
                    )
                    Text(
                        text = "Rs ${mechanic.hourlyRate.toInt()}",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DashTextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Primary.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "View →",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Primary
                    )
                }
            }
        }
    }
}

@Composable
private fun MechanicStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = DashTextPrimary
        )
        Text(
            text = label,
            fontFamily = Roboto,
            fontSize = 11.sp,
            color = DashTextLight
        )
    }
}