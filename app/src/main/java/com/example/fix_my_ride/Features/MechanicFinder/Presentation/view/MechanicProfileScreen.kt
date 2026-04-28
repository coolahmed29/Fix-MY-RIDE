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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.MechanicReview
import com.example.fix_my_ride.Features.MechanicFinder.Presentation.viewmodel.MechanicFinderViewModel
import com.example.fix_my_ride.ui.theme.DashBackground
import com.example.fix_my_ride.ui.theme.DashCardBg
import com.example.fix_my_ride.ui.theme.DashTextLight
import com.example.fix_my_ride.ui.theme.DashTextPrimary
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
fun MechanicProfileScreen(
    mechanic: Mechanic?,
    onBack: () -> Unit,
    onServiceRequest: () -> Unit,  // ✅ Service request button
    onChatClick: (vendorId: String, vendorName: String) -> Unit,  // ✅ Chat lambda
    viewModel: MechanicFinderViewModel = hiltViewModel()
) {
    if (mechanic == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Primary)
        }
        return
    }

    LaunchedEffect(mechanic.id) {
        viewModel.getMechanicProfile(mechanic.id)
    }

    val profileState by viewModel.mechanicProfileState
        .collectAsStateWithLifecycle()

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
                MechanicProfileHeader(mechanic = mechanic, onBack = onBack)
            }

            item(key = "profile_card") {
                MechanicProfileCard(
                    mechanic = mechanic,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item(key = "contact_section") {
                ContactSection(
                    mechanic = mechanic,
                    onChatClick = onChatClick,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            item(key = "specializations") {
                SpecializationsSection(
                    specializations = mechanic.specializations,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            reviewsSection(
                reviews = mechanic.reviews,
                mechanic = mechanic
            )

            item(key = "action_buttons") {
                ActionButtonsSection(
                    onServiceRequest = onServiceRequest,
                    onChat = { onChatClick(mechanic.id, mechanic.name) },
                    isAvailable = mechanic.isAvailable,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
        }
    }
}

// ── Header ────────────────────────────────────────────
@Composable
private fun MechanicProfileHeader(
    mechanic: Mechanic,
    onBack: () -> Unit
) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = mechanic.name,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DashTextPrimary
                )
                if (mechanic.experience >= 5) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = Primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Text(
                text = mechanic.workshopName ?: "Professional Mechanic",
                fontFamily = Roboto,
                fontSize = 12.sp,
                color = DashTextSecondary
            )
        }
    }
}

// ── Profile Card ─────────────────────────────────────
@Composable
private fun MechanicProfileCard(
    mechanic: Mechanic,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(alpha = 0.04f))
            .clip(RoundedCornerShape(16.dp))
            .background(DashCardBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Avatar + Basic Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Primary.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mechanic.name.first().toString(),
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = Primary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${String.format("%.1f", mechanic.rating)}",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DashTextPrimary
                        )
                        Text(
                            text = " (${mechanic.reviews.size} reviews)",
                            fontFamily = Roboto,
                            fontSize = 12.sp,
                            color = DashTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${mechanic.completedJobs} jobs completed",
                        fontFamily = Roboto,
                        fontSize = 13.sp,
                        color = DashTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.Black.copy(alpha = 0.05f))
            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatColumn(label = "Experience", value = "${mechanic.experience} years")
                StatColumn(label = "Response Time", value = "${mechanic.averageResponseTime} min")
                StatColumn(label = "Hourly Rate", value = "Rs ${mechanic.hourlyRate.toInt()}")
            }

            if (mechanic.workshopAddress != null) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color.Black.copy(alpha = 0.05f))
                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Workshop Location",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = DashTextPrimary
                        )
                        Text(
                            text = mechanic.workshopAddress,
                            fontFamily = Roboto,
                            fontSize = 12.sp,
                            color = DashTextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

// ── Contact Section ──────────────────────────────────
@Composable
private fun ContactSection(
    mechanic: Mechanic,
    onChatClick: (vendorId: String, vendorName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Contact Information",
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = DashTextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Phone
        ContactCard(
            icon = Icons.Default.Call,
            label = "Phone",
            value = mechanic.phone
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email
        ContactCard(
            icon = Icons.Default.Email,
            label = "Email",
            value = mechanic.email
        )
    }
}

@Composable
private fun ContactCard(
    icon: ImageVector,
    label: String,
    value: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DashCardBg)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = label,
                    fontFamily = Roboto,
                    fontSize = 11.sp,
                    color = DashTextLight
                )
                Text(
                    text = value,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = DashTextPrimary
                )
            }
        }
    }
}

// ── Specializations Section ───────────────────────────
@Composable
private fun SpecializationsSection(
    specializations: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Specializations",
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = DashTextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DashCardBg)
                .padding(12.dp)
        ) {
            Column {
                specializations.forEach { specialization ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Primary)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = specialization,
                            fontFamily = Roboto,
                            fontSize = 13.sp,
                            color = DashTextPrimary
                        )
                    }
                }
            }
        }
    }
}

// ─��� Reviews Section ──────────────────────────────────
private fun LazyListScope.reviewsSection(
    reviews: List<MechanicReview>,
    mechanic: Mechanic
) {
    item(key = "reviews_header") {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reviews",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DashTextPrimary
                )

                if (reviews.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Primary.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${reviews.size} reviews",
                            fontFamily = Roboto,
                            fontSize = 11.sp,
                            color = Primary
                        )
                    }
                }
            }
        }
    }

    if (reviews.isEmpty()) {
        item(key = "no_reviews") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No reviews yet",
                    fontFamily = Roboto,
                    fontSize = 13.sp,
                    color = DashTextSecondary
                )
            }
        }
    } else {
        items(items = reviews, key = { it.id }) { review ->
            ReviewCard(
                review = review,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun ReviewCard(
    review: MechanicReview,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DashCardBg)
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.userName,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = DashTextPrimary
                    )
                }

                // Rating Stars
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < review.rating.toInt()) Color(0xFFFFC107) else Color.Gray.copy(alpha = 0.3f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review.comment,
                fontFamily = Roboto,
                fontSize = 12.sp,
                color = DashTextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

// ── Action Buttons Section ────────────────────────────
@Composable
private fun ActionButtonsSection(
    onServiceRequest: () -> Unit,
    onChat: () -> Unit,
    isAvailable: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(
            onClick = onServiceRequest,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            shape = RoundedCornerShape(12.dp),
            enabled = isAvailable
        ) {
            Text(
                text = if (isAvailable) "Request Service" else "Not Available",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onChat,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DashCardBg),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "💬 Chat with Mechanic",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Primary
            )
        }
    }
}

// ── Stat Column ───────────────────────────────────────
@Composable
private fun StatColumn(label: String, value: String) {
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