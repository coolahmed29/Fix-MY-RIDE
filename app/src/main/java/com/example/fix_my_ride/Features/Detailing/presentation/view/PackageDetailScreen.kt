package com.example.fix_my_ride.Features.Detailing.presentation.view

// Features/Detailing/Presentation/View/PackageDetailScreen.kt


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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.Features.Detailing.domain.model.DetailingPackage
import com.example.fix_my_ride.ui.Components.AppButton
import com.example.fix_my_ride.ui.theme.DashBackground
import com.example.fix_my_ride.ui.theme.DashCardBg
import com.example.fix_my_ride.ui.theme.DashTextLight
import com.example.fix_my_ride.ui.theme.DashTextPrimary
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.IconGreen
import com.example.fix_my_ride.ui.theme.IconYellow
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
fun PackageDetailScreen(
    pkg        : DetailingPackage,
    onBack     : () -> Unit,
    onBookNow  : () -> Unit
) {
    Scaffold(
        containerColor = DashBackground,
        bottomBar      = {
            // ── Book Now Button ────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DashBackground)
                    .padding(
                        horizontal = 20.dp,
                        vertical   = 16.dp
                    )
            ) {
                AppButton(
                    text    = "Book Now — Rs ${pkg.price.toInt()}",
                    onClick = onBookNow
                )
            }
        }
    ) { padding ->

        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            // ── Header ─────────────────────────────────
            item(key = "header") {
                PackageDetailHeader(
                    pkg    = pkg,
                    onBack = onBack
                )
            }

            // ── Stats Row ──────────────────────────────
            item(key = "stats") {
                PackageStatsRow(
                    pkg      = pkg,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical   = 8.dp
                    )
                )
            }

            // ── Description ────────────────────────────
            item(key = "description") {
                SectionCard(
                    title    = "Description",
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical   = 8.dp
                    )
                ) {
                    Text(
                        text       = pkg.description,
                        fontFamily = Roboto,
                        fontSize   = 14.sp,
                        color      = DashTextSecondary,
                        lineHeight = 22.sp
                    )
                }
            }

            // ── Included Services ──────────────────────
            item(key = "services_title") {
                Text(
                    text       = "Included Services",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                    color      = DashTextPrimary,
                    modifier   = Modifier.padding(
                        start  = 20.dp,
                        end    = 20.dp,
                        top    = 16.dp,
                        bottom = 8.dp
                    )
                )
            }

            items(
                items = pkg.includedServices,
                key   = { "service_$it" }
            ) { service ->
                ServiceItem(
                    service  = service,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical   = 4.dp
                    )
                )
            }

            item(key = "bottom_space") {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ── Package Detail Header ─────────────────────────────
@Composable
private fun PackageDetailHeader(
    pkg    : DetailingPackage,
    onBack : () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Primary.copy(alpha = 0.05f))
            .padding(20.dp)
    ) {
        Column {
            // Back button
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
                        imageVector        = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back",
                        tint               = DashTextPrimary,
                        modifier           = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Type badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Primary.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Text(
                    text       = pkg.type.displayName,
                    fontFamily = Roboto,
                    fontSize   = 12.sp,
                    color      = Primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Package name
            Text(
                text       = pkg.name,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize   = 24.sp,
                color      = DashTextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Workshop name
            Text(
                text       = pkg.workshopName,
                fontFamily = Roboto,
                fontSize   = 13.sp,
                color      = DashTextSecondary
            )
        }
    }
}

// ── Stats Row ─────────────────────────────────────────
@Composable
private fun PackageStatsRow(
    pkg      : DetailingPackage,
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 4.dp,
                shape        = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(DashCardBg)
            .padding(16.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            StatColumn(label = "Price",    value = "Rs ${pkg.price.toInt()}")
            StatColumn(label = "Duration", value = "${pkg.durationHours}h")
            StatColumn(label = "Rating",   value = "⭐ ${pkg.rating}")
        }
    }
}

@Composable
private fun StatColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text       = value,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize   = 16.sp,
            color      = DashTextPrimary
        )
        Text(
            text       = label,
            fontFamily = Roboto,
            fontSize   = 11.sp,
            color      = DashTextLight
        )
    }
}

// ── Section Card ──────────────────────────────────────
@Composable
private fun SectionCard(
    title    : String,
    modifier : Modifier = Modifier,
    content  : @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 4.dp,
                shape        = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(DashCardBg)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text       = title,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 14.sp,
                color      = DashTextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color.Black.copy(alpha = 0.05f))
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

// ── Service Item ──────────────────────────────────────
@Composable
private fun ServiceItem(
    service  : String,
    modifier : Modifier = Modifier
) {
    Row(
        modifier          = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector        = Icons.Default.CheckCircle,
            contentDescription = null,
            tint               = IconGreen,
            modifier           = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text       = service,
            fontFamily = Roboto,
            fontSize   = 14.sp,
            color      = DashTextPrimary
        )
    }
}