package com.example.fix_my_ride.ui.Components.Dashboard


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Roboto

// ── Stat Card — reusable across ANY dashboard ─────────────────────────────────
// Usage: VendorDashboard, DriverDashboard, AdminDashboard, etc.
@Composable
fun DsStatCard(
    icon     : ImageVector,
    label    : String,
    value    : String,
    subLabel : String,
    color    : Color,
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(Radius.LG.dp))
            .background(AppColors.CardBg)
            .padding(14.dp)
    ) {
        Column {
            Box(
                modifier         = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(Radius.SM.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(value,    fontFamily = Montserrat, fontWeight = FontWeight.Bold,      fontSize = 20.sp, color = AppColors.TextPrimary)
            Text(label,    fontFamily = Roboto,     fontSize = 11.sp,                  color = AppColors.TextSecondary)
            Text(subLabel, fontFamily = Roboto,     fontSize = 10.sp,                  color = color)
        }
    }
}

// ── Quick Action Card — reusable ──────────────────────────────────────────────
@Composable
fun DsQuickActionCard(
    icon     : ImageVector,
    label    : String,
    color    : Color,
    onClick  : () -> Unit,
    modifier : Modifier = Modifier
) {
    Column(
        modifier            = modifier
            .clip(RoundedCornerShape(Radius.MD.dp))
            .background(AppColors.CardBg)
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier         = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(Radius.SM.dp))
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(label, fontFamily = Roboto, fontSize = 11.sp, color = AppColors.TextSecondary, textAlign = TextAlign.Center)
    }
}

// ── Smart Alert — reusable ────────────────────────────────────────────────────
@Composable
fun DsSmartAlert(
    icon     : ImageVector,
    message  : String,
    color    : Color,
    onClick  : () -> Unit,
    modifier : Modifier = Modifier,
    trailingIcon : ImageVector? = null  // optional chevron
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.MD.dp))
            .background(color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(Radius.MD.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text(message, fontFamily = Roboto, fontSize = 13.sp, color = color, modifier = Modifier.weight(1f))
        trailingIcon?.let {
            Icon(it, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
        }
    }
}

// ── Welcome Header — reusable ─────────────────────────────────────────────────
// Pass any role/greeting string — works for Vendor, Driver, Admin
@Composable
fun DsWelcomeHeader(
    greeting  : String = "Welcome back,",
    userName  : String,
    modifier  : Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(greeting,  fontFamily = Roboto,      fontSize = 13.sp, color = AppColors.TextSecondary)
        Text(userName,  fontFamily = Montserrat,  fontWeight = FontWeight.Bold, fontSize = 22.sp, color = AppColors.TextPrimary)
    }
}