package com.example.fix_my_ride.ui.Components.Dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.Dashboards.Vendor.Domain.model.OrderStatus
import com.example.fix_my_ride.Dashboards.Vendor.Domain.model.VendorEarning
import com.example.fix_my_ride.Dashboards.Vendor.Domain.model.VendorOrder
import com.example.fix_my_ride.Dashboards.Vendor.Domain.model.VendorProduct
import com.example.fix_my_ride.ui.Components.Dashboard.AppColors
import com.example.fix_my_ride.ui.Components.Dashboard.Radius
import com.example.fix_my_ride.ui.Components.Dashboard.*
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
internal fun VendorDashboardTab(
    isOpen      : Boolean,
    onToggle    : () -> Unit,
    products    : List<VendorProduct>,
    orders      : List<VendorOrder>,
    earnings    : List<VendorEarning>,
    total       : Double,
    lowStock    : Int,
    onTabChange : (Int) -> Unit,
    modifier    : Modifier = Modifier
) {
    LazyColumn(
        modifier       = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {

        item {
            Spacer(Modifier.height(16.dp))
            DsWelcomeHeader(userName = "Ahmed")
            Spacer(Modifier.height(16.dp))
        }

        // Hero Earnings Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.linearGradient(listOf(AppColors.Primary, AppColors.Primary.copy(alpha = 0.7f))))
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Earnings", fontFamily = Roboto, fontSize = 13.sp, color = Color.White.copy(0.8f))
                            Text(
                                "Rs ${"%,.0f".format(total)}",
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 28.sp,
                                color      = Color.White
                            )
                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TrendingUp, null, tint = Color.White.copy(0.9f), modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("+18% from last month", fontFamily = Roboto, fontSize = 11.sp, color = Color.White.copy(0.9f))
                            }
                        }
                        DsMiniBarChart(
                            entries  = earnings.takeLast(5).map { ChartEntry(it.month, it.amount) },
                            barColor = Color.White.copy(0.6f)
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // Shop toggle
                    Row(
                        modifier              = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(0.15f))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isOpen) AppColors.Success else Color.Gray)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text       = if (isOpen) "Shop Open" else "Shop Closed",
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                                fontSize   = 13.sp,
                                color      = Color.White
                            )
                        }
                        Switch(
                            checked         = isOpen,
                            onCheckedChange = { onToggle() },
                            colors          = SwitchDefaults.colors(
                                checkedThumbColor   = Color.White,
                                checkedTrackColor   = AppColors.Success,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }

        // Quick Actions
        item {
            Spacer(Modifier.height(16.dp))
            Text(
                "Quick Actions",
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 14.sp,
                color      = AppColors.TextPrimary,
                modifier   = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(10.dp))
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DsQuickActionCard(Icons.Default.Add,          "Add Product", AppColors.Primary, { onTabChange(1) }, Modifier.weight(1f))
                DsQuickActionCard(Icons.Default.ShoppingBag,  "View Orders", AppColors.Warning, { onTabChange(2) }, Modifier.weight(1f))
                DsQuickActionCard(Icons.Default.BarChart,     "Earnings",    AppColors.Success, { onTabChange(3) }, Modifier.weight(1f))
            }
        }

        // Stats Row
        item {
            Spacer(Modifier.height(16.dp))
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DsStatCard(Icons.Default.Inventory2,  "Products",  products.count { it.isAvailable }.toString(),                          "active",  AppColors.Primary,  Modifier.weight(1f))
                DsStatCard(Icons.Default.ShoppingBag, "Orders",    orders.count { it.status == OrderStatus.PENDING }.toString(), "pending", AppColors.Warning,  Modifier.weight(1f))
                DsStatCard(Icons.Default.Warning,     "Low Stock", lowStock.toString(),                                                    "items",   AppColors.Error,    Modifier.weight(1f))
            }
        }

        // Smart Alerts
        val pendingCount = orders.count { it.status == OrderStatus.PENDING }
        if (pendingCount > 0) {
            item {
                Spacer(Modifier.height(16.dp))
                DsSmartAlert(
                    icon         = Icons.Default.Notifications,
                    message      = "You have $pendingCount pending order${if (pendingCount > 1) "s" else ""} — process now!",
                    color        = AppColors.Warning,
                    onClick      = { onTabChange(2) },
                    modifier     = Modifier.padding(horizontal = 16.dp),
                    trailingIcon = Icons.Default.ArrowForwardIos
                )
            }
        }
        if (lowStock > 0) {
            item {
                Spacer(Modifier.height(8.dp))
                DsSmartAlert(
                    icon         = Icons.Default.Warning,
                    message      = "$lowStock product${if (lowStock > 1) "s" else ""} low on stock!",
                    color        = AppColors.Error,
                    onClick      = { onTabChange(1) },
                    modifier     = Modifier.padding(horizontal = 16.dp),
                    trailingIcon = Icons.Default.ArrowForwardIos
                )
            }
        }

        // Recent Orders header
        item {
            Spacer(Modifier.height(20.dp))
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Recent Orders", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AppColors.TextPrimary)
                Text("See all", fontFamily = Roboto, fontSize = 12.sp, color = AppColors.Primary, modifier = Modifier.clickable { onTabChange(2) })
            }
            Spacer(Modifier.height(10.dp))
        }

        // Recent Orders list — NOTE: items() not nested inside item()
        val recent = orders.take(4)
        items(recent) { order ->
            OrderCardMini(
                order    = order,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}