package com.example.fix_my_ride.ui.Components.Dashboard


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Roboto

// ── Primary Full-Width Button ─────────────────────────────────────────────────
@Composable
fun DsPrimaryButton(
    text     : String,
    onClick  : () -> Unit,
    modifier : Modifier = Modifier,
    enabled  : Boolean = true,
    icon     : ImageVector? = null,
    color    : Color = AppColors.Primary
) {
    Button(
        onClick  = onClick,
        enabled  = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape  = RoundedCornerShape(Radius.MD.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        icon?.let {
            Icon(it, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.White)
    }
}

// ── Confirm / Danger row buttons ──────────────────────────────────────────────
@Composable
fun DsConfirmButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick  = onClick,
        modifier = modifier,
        shape    = RoundedCornerShape(Radius.SM.dp),
        colors   = ButtonDefaults.buttonColors(containerColor = AppColors.Success)
    ) {
        Text(text, fontFamily = Roboto, color = Color.White, fontSize = 13.sp)
    }
}

@Composable
fun DsCancelButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick  = onClick,
        modifier = modifier,
        shape    = RoundedCornerShape(Radius.SM.dp)
    ) {
        Text(text, fontFamily = Roboto, color = AppColors.Error, fontSize = 13.sp)
    }
}