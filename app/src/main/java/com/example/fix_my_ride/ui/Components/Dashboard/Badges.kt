package com.example.fix_my_ride.ui.Components.Dashboard


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.ui.theme.Roboto

// ── Generic Status Badge ───────────────────────────────────────────────────────
// Pass any label+color — works for OrderStatus, DriverStatus, BookingStatus, etc.
@Composable
fun DsStatusBadge(
    label : String,
    color : Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Radius.Pill.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text       = label,
            fontFamily = Roboto,
            fontWeight = FontWeight.Medium,
            fontSize   = 11.sp,
            color      = color
        )
    }
}

// ── Generic Chip ───────────────────────────────────────────────────────────────
@Composable
fun DsChip(
    label : String,
    color : Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Radius.Pill.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text       = label,
            fontFamily = Roboto,
            fontSize   = 11.sp,
            color      = color,
            fontWeight = FontWeight.Medium
        )
    }
}