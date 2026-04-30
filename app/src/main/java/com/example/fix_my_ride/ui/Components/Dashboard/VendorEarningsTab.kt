package com.example.fix_my_ride.ui.Components.Dashboard


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.Dashboards.Vendor.Domain.model.VendorEarning
import com.example.fix_my_ride.ui.Components.Dashboard.AppColors
import com.example.fix_my_ride.ui.Components.Dashboard.ChartEntry
import com.example.fix_my_ride.ui.Components.Dashboard.DsBarChart
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
internal fun VendorEarningsTab(
    earnings : List<VendorEarning>,
    total    : Double,
    modifier : Modifier = Modifier
) {
    LazyColumn(
        modifier            = modifier.fillMaxSize(),
        contentPadding      = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Earnings", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = AppColors.TextPrimary)
        }

        // Total card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(AppColors.Primary, AppColors.Primary.copy(0.75f))))
                    .padding(24.dp)
            ) {
                Column {
                    Text("Total Earnings", fontFamily = Roboto, fontSize = 13.sp, color = Color.White.copy(0.8f))
                    Spacer(Modifier.height(4.dp))
                    Text("Rs ${"%,.0f".format(total)}", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Color.White)
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrendingUp, null, tint = Color.White.copy(0.9f), modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("+18% from last month", fontFamily = Roboto, fontSize = 11.sp, color = Color.White.copy(0.9f))
                    }
                }
            }
        }

        // Bar chart card — using DsBarChart from design system
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppColors.CardBg)
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text("Monthly Earnings", fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = AppColors.TextPrimary)
                        Text("2026", fontFamily = Roboto, fontSize = 12.sp, color = AppColors.TextSecondary)
                    }
                    Spacer(Modifier.height(20.dp))
                    DsBarChart(
                        entries     = earnings.map { ChartEntry(it.month, it.amount) },
                        chartHeight = 120
                    )
                }
            }
        }

        item {
            Text("Monthly Breakdown", fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = AppColors.TextPrimary)
        }

        itemsIndexed(earnings) { _, e ->
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(AppColors.CardBg)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(e.month,  fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = AppColors.TextPrimary)
                Text("Rs ${"%,.0f".format(e.amount)}", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AppColors.Primary)
            }
        }
    }
}