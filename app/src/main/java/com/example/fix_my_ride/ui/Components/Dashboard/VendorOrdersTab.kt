package com.example.fix_my_ride.ui.Components.Dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.Dashboards.Vendor.Domain.model.OrderStatus
import com.example.fix_my_ride.Dashboards.Vendor.Domain.model.VendorOrder
import com.example.fix_my_ride.ui.Components.Dashboard.AppColors
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
internal fun OrdersTab(
    orders    : List<VendorOrder>,
    onConfirm : (String) -> Unit,
    onCancel  : (String) -> Unit,
    modifier  : Modifier = Modifier
) {
    val pending = orders.filter { it.status == OrderStatus.PENDING }
    val others  = orders.filter { it.status != OrderStatus.PENDING }

    LazyColumn(
        modifier            = modifier.fillMaxSize(),
        contentPadding      = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Orders", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = AppColors.TextPrimary)
            Spacer(Modifier.height(12.dp))
        }

        if (orders.isEmpty()) {
            item {
                Box(
                    modifier         = Modifier.fillMaxWidth().padding(vertical = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.ShoppingBag, null, tint = Color.Gray.copy(0.4f), modifier = Modifier.size(52.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("No orders yet", fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = AppColors.TextSecondary)
                        Text("Orders will appear here", fontFamily = Roboto, fontSize = 13.sp, color = AppColors.TextLight)
                    }
                }
            }
        } else {
            if (pending.isNotEmpty()) {
                item {
                    Text("Pending (${pending.size})", fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = AppColors.Warning)
                    Spacer(Modifier.height(8.dp))
                }
                items(pending) { order ->
                    OrderCard(
                        order     = order,
                        onConfirm = { onConfirm(order.id) },
                        onCancel  = { onCancel(order.id) }
                    )
                }
            }
            if (others.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Text("Previous", fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = AppColors.TextSecondary)
                    Spacer(Modifier.height(8.dp))
                }
                items(others) { order ->
                    OrderCard(order = order, onConfirm = {}, onCancel = {})
                }
            }
        }
    }
}