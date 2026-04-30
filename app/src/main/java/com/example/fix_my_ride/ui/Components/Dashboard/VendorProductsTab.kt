package com.example.fix_my_ride.ui.Components.Dashboard


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.Dashboards.Vendor.Domain.model.VendorProduct
import com.example.fix_my_ride.ui.Components.Dashboard.AppColors
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
internal fun ProductsTab(
    products : List<VendorProduct>,
    onToggle : (String) -> Unit,
    modifier : Modifier = Modifier
) {
    LazyColumn(
        modifier            = modifier.fillMaxSize(),
        contentPadding      = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("My Products", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = AppColors.TextPrimary)
            Spacer(Modifier.height(4.dp))
            Text(
                "${products.count { it.isAvailable }} active • ${products.count { it.stock == 0 }} out of stock",
                fontFamily = Roboto, fontSize = 13.sp, color = AppColors.TextSecondary
            )
            Spacer(Modifier.height(8.dp))
        }

        if (products.isEmpty()) {
            item {
                Box(
                    modifier         = Modifier.fillMaxWidth().padding(vertical = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Inventory2, null, tint = Color.Gray.copy(0.4f), modifier = Modifier.size(52.dp))
                        Spacer(Modifier.height(12.dp))
                        Text("No products yet", fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = AppColors.TextSecondary)
                        Text("Tap + to add your first product", fontFamily = Roboto, fontSize = 13.sp, color = AppColors.TextLight)
                    }
                }
            }
        } else {
            items(products) { product ->
                ProductCard(
                    product  = product,
                    onToggle = { onToggle(product.id) }
                )
            }
        }
    }
}