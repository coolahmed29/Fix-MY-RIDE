package com.example.fix_my_ride.ui.Components

// ui/Components/CategoryChip.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
fun CategoryChip(
    label      : String,
    isSelected : Boolean,
    onClick    : () -> Unit,
    modifier   : Modifier = Modifier
) {
    Box(
        modifier         = modifier
            .clip(RoundedCornerShape(30.dp))
            .background(
                if (isSelected) Primary
                else Primary.copy(alpha = 0.08f)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = label,
            fontFamily = Roboto,
            fontWeight = if (isSelected) FontWeight.SemiBold
            else FontWeight.Normal,
            fontSize   = 13.sp,
            color      = if (isSelected) androidx.compose.ui.graphics.Color.White
            else DashTextSecondary
        )
    }
}