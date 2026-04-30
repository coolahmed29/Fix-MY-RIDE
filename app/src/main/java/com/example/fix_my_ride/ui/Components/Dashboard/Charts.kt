package com.example.fix_my_ride.ui.Components.Dashboard


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.ui.theme.Roboto

// Data class — generic, works for earnings, trips, bookings, etc.
data class ChartEntry(
    val label  : String,   // "Jan", "Mon", "Week 1" — anything
    val amount : Double
)

// ── Full Bar Chart ────────────────────────────────────────────────────────────
@Composable
fun DsBarChart(
    entries      : List<ChartEntry>,
    barColor     : Brush = Brush.verticalGradient(listOf(AppColors.Primary, AppColors.Primary.copy(0.4f))),
    chartHeight  : Int   = 120,
    modifier     : Modifier = Modifier
) {
    val max = entries.maxOfOrNull { it.amount } ?: 1.0

    Row(
        modifier              = modifier
            .fillMaxWidth()
            .height(chartHeight.dp),
        verticalAlignment     = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        entries.forEachIndexed { index, e ->
            val fraction = (e.amount / max).toFloat()
            val animHeight by animateFloatAsState(
                targetValue   = (chartHeight - 20f) * fraction,
                animationSpec = tween(600, delayMillis = index * 80),
                label         = "bar_$index"
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier            = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(animHeight.dp)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(barColor)
                )
                Spacer(Modifier.height(6.dp))
                Text(e.label, fontFamily = Roboto, fontSize = 10.sp, color = AppColors.TextLight)
            }
        }
    }
}

// ── Mini Bar Chart (for hero cards) ──────────────────────────────────────────
@Composable
fun DsMiniBarChart(
    entries   : List<ChartEntry>,
    barColor  : Color = Color.White.copy(0.6f),
    maxHeight : Float = 50f
) {
    val max = entries.maxOfOrNull { it.amount } ?: 1.0

    Row(
        verticalAlignment     = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        entries.forEachIndexed { index, e ->
            val fraction = (e.amount / max).toFloat()
            val animHeight by animateFloatAsState(
                targetValue   = maxHeight * fraction,
                animationSpec = tween(600, delayMillis = index * 80),
                label         = "mini_$index"
            )
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(animHeight.dp)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(barColor)
            )
        }
    }
}