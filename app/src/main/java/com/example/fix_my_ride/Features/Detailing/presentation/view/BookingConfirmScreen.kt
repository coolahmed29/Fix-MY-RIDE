package com.example.fix_my_ride.Features.Detailing.presentation.view

// Features/Detailing/Presentation/View/BookingConfirmScreen.kt

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.Features.Detailing.domain.model.Booking
import com.example.fix_my_ride.ui.Components.AppButton
import com.example.fix_my_ride.ui.theme.DashBackground
import com.example.fix_my_ride.ui.theme.DashCardBg
import com.example.fix_my_ride.ui.theme.DashTextLight
import com.example.fix_my_ride.ui.theme.DashTextPrimary
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.IconGreen
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
fun BookingConfirmScreen(
    booking       : Booking,
    onGoHome      : () -> Unit
) {
    Column(
        modifier            = Modifier
            .fillMaxSize()
            .background(DashBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ── Success Icon ──────────────────────────────
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(IconGreen.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = Icons.Default.CheckCircle,
                contentDescription = null,
                tint               = IconGreen,
                modifier           = Modifier.size(52.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── Title ─────────────────────────────────────
        Text(
            text       = "Booking Confirmed!",
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize   = 24.sp,
            color      = DashTextPrimary,
            textAlign  = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text       = "Your booking has been successfully placed",
            fontFamily = Roboto,
            fontSize   = 14.sp,
            color      = DashTextSecondary,
            textAlign  = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ── Booking Summary Card ───────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation    = 4.dp,
                    shape        = RoundedCornerShape(20.dp),
                    ambientColor = Color.Black.copy(alpha = 0.04f)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(DashCardBg)
                .padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryRow(
                    label = "Package",
                    value = booking.packageName
                )
                HorizontalDivider(
                    color = Color.Black.copy(alpha = 0.05f)
                )
                SummaryRow(
                    label = "Date",
                    value = booking.date
                )
                HorizontalDivider(
                    color = Color.Black.copy(alpha = 0.05f)
                )
                SummaryRow(
                    label = "Time",
                    value = booking.timeSlot
                )
                HorizontalDivider(
                    color = Color.Black.copy(alpha = 0.05f)
                )
                SummaryRow(
                    label = "Workshop",
                    value = booking.workshopName
                )
                HorizontalDivider(
                    color = Color.Black.copy(alpha = 0.05f)
                )
                SummaryRow(
                    label      = "Price",
                    value      = "Rs ${booking.price.toInt()}",
                    valueColor = Primary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ── Go Home Button ────────────────────────────
        AppButton(
            text    = "Back to Home",
            onClick = onGoHome
        )
    }
}

// ── Summary Row ───────────────────────────────────────
@Composable
private fun SummaryRow(
    label      : String,
    value      : String,
    valueColor : Color = DashTextPrimary
) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text       = label,
            fontFamily = Roboto,
            fontSize   = 13.sp,
            color      = DashTextLight
        )
        Text(
            text       = value,
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 14.sp,
            color      = valueColor
        )
    }
}