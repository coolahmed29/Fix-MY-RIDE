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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceType
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

@Composable
fun ServiceRequestScreen(
    mechanic: Mechanic,
    userId: String,
    userLatitude: Double,
    userLongitude: Double,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: MechanicFinderViewModel = hiltViewModel()
) {
    var selectedServiceType by remember { mutableStateOf(ServiceType.GENERAL_MAINTENANCE) }
    var description by remember { mutableStateOf("") }
    var carModel by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val serviceRequestState by viewModel.serviceRequestState
        .collectAsStateWithLifecycle()

    // Handle success response
    if (serviceRequestState is AuthResult.Success<*>) {
        onSuccess()
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
                ServiceRequestHeader(onBack = onBack)
            }

            item(key = "mechanic_info") {
                MechanicSelectionCard(
                    mechanic = mechanic,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item(key = "service_type") {
                ServiceTypeSelectionSection(
                    selectedServiceType = selectedServiceType,
                    onServiceTypeSelected = { selectedServiceType = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item(key = "car_info") {
                CarInformationSection(
                    carModel = carModel,
                    onCarModelChanged = { carModel = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item(key = "location_info") {
                LocationInformationSection(
                    address = address,
                    onAddressChanged = { address = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item(key = "description") {
                DescriptionSection(
                    description = description,
                    onDescriptionChanged = { description = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item(key = "price_estimate") {
                PriceEstimateCard(
                    mechanic = mechanic,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item(key = "action_buttons") {
                ActionButtons(
                    isLoading = isLoading || (serviceRequestState is AuthResult.Loading),
                    onSubmit = {
                        if (carModel.isEmpty() || address.isEmpty()) {
                            return@ActionButtons
                        }

                        isLoading = true
                        val serviceRequest = ServiceRequest(
                            id = "",
                            userId = userId,
                            mechanicId = mechanic.id,
                            serviceType = selectedServiceType,
                            status = com.example.fix_my_ride.Features.MechanicFinder.domain.model.RequestStatus.PENDING,
                            description = description,
                            latitude = userLatitude,
                            longitude = userLongitude,
                            address = address,
                            carModel = carModel,
                            requestedTime = System.currentTimeMillis(),
                            estimatedCost = mechanic.hourlyRate,
                            actualCost = null,
                            completedTime = null,
                            rating = null,
                            feedback = null,
                            images = emptyList(),
                            estimatedDuration = null,
                            mechanicAcceptedTime = null,
                            mechanicArrivedTime = null,
                            serviceStartTime = null,
                            serviceEndTime = null
                        )
                        viewModel.sendServiceRequest(serviceRequest)
                    },
                    onCancel = onBack,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
        }
    }
}

// ── Header ────────────────────────────────────────────
@Composable
private fun ServiceRequestHeader(onBack: () -> Unit) {
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
                text = "Request Service",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DashTextPrimary
            )
            Text(
                text = "Book mechanic for repair",
                fontFamily = Roboto,
                fontSize = 12.sp,
                color = DashTextSecondary
            )
        }
    }
}

// ── Mechanic Selection Card ───────────────────────────
@Composable
private fun MechanicSelectionCard(
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
                        text = " • ${mechanic.experience} years",
                        fontFamily = Roboto,
                        fontSize = 12.sp,
                        color = DashTextSecondary
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Primary.copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "✓ Selected",
                    fontFamily = Roboto,
                    fontSize = 11.sp,
                    color = Primary
                )
            }
        }
    }
}

// ── Service Type Selection ────────────────────────────
@Composable
private fun ServiceTypeSelectionSection(
    selectedServiceType: ServiceType,
    onServiceTypeSelected: (ServiceType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Service Type",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DashTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DashCardBg)
                .clickable { }
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = selectedServiceType.displayName,
                        fontFamily = Roboto,
                        fontSize = 14.sp,
                        color = DashTextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = selectedServiceType.icon,
                        fontSize = 16.sp
                    )
                }
                Text(
                    text = "▼",
                    fontSize = 14.sp,
                    color = DashTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap to select service type",
            fontFamily = Roboto,
            fontSize = 11.sp,
            color = DashTextLight
        )
    }
}

// ── Car Information Section ��──────────────────────────
@Composable
private fun CarInformationSection(
    carModel: String,
    onCarModelChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Car Information",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DashTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = carModel,
            onValueChange = onCarModelChanged,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = {
                Text(
                    text = "e.g., Honda Civic 2020",
                    fontFamily = Roboto,
                    fontSize = 13.sp,
                    color = DashTextLight
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = DashTextPrimary,
                unfocusedTextColor = DashTextPrimary,
                focusedBorderColor = Primary,
                unfocusedBorderColor = DashCardBg,
                focusedContainerColor = DashCardBg,
                unfocusedContainerColor = DashCardBg
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = Roboto,
                fontSize = 14.sp
            )
        )
    }
}

// ── Location Information Section ──────────────────────
@Composable
private fun LocationInformationSection(
    address: String,
    onAddressChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Location",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DashTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = address,
            onValueChange = onAddressChanged,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            placeholder = {
                Text(
                    text = "Enter service location",
                    fontFamily = Roboto,
                    fontSize = 13.sp,
                    color = DashTextLight
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = DashTextPrimary,
                unfocusedTextColor = DashTextPrimary,
                focusedBorderColor = Primary,
                unfocusedBorderColor = DashCardBg,
                focusedContainerColor = DashCardBg,
                unfocusedContainerColor = DashCardBg
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = Roboto,
                fontSize = 14.sp
            )
        )
    }
}

// ── Description Section ───────────────────────────────
@Composable
private fun DescriptionSection(
    description: String,
    onDescriptionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Problem Description",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DashTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChanged,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            placeholder = {
                Text(
                    text = "Describe the issue or service needed...",
                    fontFamily = Roboto,
                    fontSize = 13.sp,
                    color = DashTextLight
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = DashTextPrimary,
                unfocusedTextColor = DashTextPrimary,
                focusedBorderColor = Primary,
                unfocusedBorderColor = DashCardBg,
                focusedContainerColor = DashCardBg,
                unfocusedContainerColor = DashCardBg
            ),
            shape = RoundedCornerShape(12.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontFamily = Roboto,
                fontSize = 14.sp
            )
        )
    }
}

// ── Price Estimate Card ───────────────────────────────
@Composable
private fun PriceEstimateCard(
    mechanic: Mechanic,
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
        Column {
            Text(
                text = "Estimated Cost",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DashTextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hourly Rate",
                        fontFamily = Roboto,
                        fontSize = 12.sp,
                        color = DashTextSecondary
                    )
                    Text(
                        text = "Rs ${mechanic.hourlyRate.toInt()}/hour",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Primary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Response Time",
                        fontFamily = Roboto,
                        fontSize = 12.sp,
                        color = DashTextSecondary
                    )
                    Text(
                        text = "~${mechanic.averageResponseTime} minutes",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Primary.copy(alpha = 0.1f))
                    .padding(8.dp)
            ) {
                Text(
                    text = "ℹ️ Final cost may vary based on actual work required",
                    fontFamily = Roboto,
                    fontSize = 11.sp,
                    color = DashTextSecondary
                )
            }
        }
    }
}

// ── Action Buttons ────────────────────────────────────
@Composable
private fun ActionButtons(
    isLoading: Boolean,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "Send Request",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DashCardBg),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Cancel",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Primary
            )
        }
    }
}