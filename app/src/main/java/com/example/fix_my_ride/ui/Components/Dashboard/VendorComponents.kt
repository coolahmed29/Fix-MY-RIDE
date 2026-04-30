package com.example.fix_my_ride.ui.Components.Dashboard


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.Dashboards.Vendor.Domain.model.OrderStatus
import com.example.fix_my_ride.Dashboards.Vendor.Domain.model.VendorOrder
import com.example.fix_my_ride.Dashboards.Vendor.Domain.model.VendorProduct
import com.example.fix_my_ride.ui.Components.Dashboard.AppColors
import com.example.fix_my_ride.ui.Components.Dashboard.Radius
import com.example.fix_my_ride.ui.Components.Dashboard.DsChip
import com.example.fix_my_ride.ui.Components.Dashboard.DsPrimaryButton
import com.example.fix_my_ride.ui.Components.Dashboard.DsStatusBadge
import com.example.fix_my_ride.ui.Components.Dashboard.DsTextField
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Roboto

// ── Onboarding Empty State ─────────────────────────────────────────────────────
@Composable
internal fun EmptyStateOnboarding(
    onAddClick : () -> Unit,
    modifier   : Modifier = Modifier
) {
    Column(
        modifier            = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier         = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(AppColors.Primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Storefront, null, tint = AppColors.Primary, modifier = Modifier.size(52.dp))
        }
        Spacer(Modifier.height(24.dp))
        Text("Start your shop 🚀", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = AppColors.TextPrimary, textAlign = TextAlign.Center)
        Spacer(Modifier.height(10.dp))
        Text("Add your first product to start selling\nand managing orders.", fontFamily = Roboto, fontSize = 14.sp, color = AppColors.TextSecondary, textAlign = TextAlign.Center, lineHeight = 22.sp)
        Spacer(Modifier.height(32.dp))
        DsPrimaryButton(text = "Add First Product", onClick = onAddClick, icon = Icons.Default.Add)
    }
}

// ── Add Product Dialog ─────────────────────────────────────────────────────────
@Composable
internal fun AddProductDialog(
    onDismiss : () -> Unit,
    onSave    : (name: String, category: String, stock: Int, price: Double) -> Unit
) {
    var name     by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var stock    by remember { mutableStateOf("") }
    var price    by remember { mutableStateOf("") }

    val isValid = name.isNotBlank() && category.isNotBlank()
            && stock.toIntOrNull() != null && price.toDoubleOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = AppColors.CardBg,
        title = {
            Text("Add Product", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = AppColors.TextPrimary)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DsTextField(value = name,     onValueChange = { name = it },     label = "Product Name")
                DsTextField(value = category, onValueChange = { category = it }, label = "Category (e.g. Engine, Brakes)")
                DsTextField(value = stock,    onValueChange = { stock = it },    label = "Stock Quantity", keyboardType = KeyboardType.Number)
                DsTextField(value = price,    onValueChange = { price = it },    label = "Price (Rs)",     keyboardType = KeyboardType.Decimal)
            }
        },
        confirmButton = {
            Button(
                onClick  = { onSave(name, category, stock.toIntOrNull() ?: 0, price.toDoubleOrNull() ?: 0.0) },
                enabled  = isValid,
                shape    = RoundedCornerShape(Radius.SM.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
            ) {
                Text("Save", fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", fontFamily = Roboto, color = AppColors.TextSecondary)
            }
        }
    )
}

// ── Product Card ───────────────────────────────────────────────────────────────
@Composable
internal fun ProductCard(
    product  : VendorProduct,
    onToggle : () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.LG.dp))
            .background(AppColors.CardBg)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(product.name, fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = AppColors.TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(product.category, fontFamily = Roboto, fontSize = 12.sp, color = AppColors.TextSecondary)
                }
                Switch(
                    checked         = product.isAvailable,
                    onCheckedChange = { onToggle() },
                    colors          = SwitchDefaults.colors(
                        checkedTrackColor   = AppColors.Success,
                        uncheckedTrackColor = Color.Gray.copy(0.3f)
                    )
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                DsChip("Rs ${product.price.toInt()}", AppColors.Primary)
                DsChip(
                    label = if (product.stock == 0) "Out of Stock" else "${product.stock} in stock",
                    color = when {
                        product.stock == 0  -> AppColors.Error
                        product.stock <= 5  -> AppColors.Warning
                        else                -> AppColors.Success
                    }
                )
            }
        }
    }
}

// ── Order Card (full) ──────────────────────────────────────────────────────────
@Composable
internal fun OrderCard(
    order     : VendorOrder,
    onConfirm : () -> Unit,
    onCancel  : () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.LG.dp))
            .background(AppColors.CardBg)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(order.customerName, fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = AppColors.TextPrimary)
                VendorOrderStatusBadge(order.status)
            }
            Text("${order.partName} × ${order.quantity}", fontFamily = Roboto, fontSize = 13.sp, color = AppColors.TextSecondary)
            Text("📅 ${order.date}", fontFamily = Roboto, fontSize = 12.sp, color = AppColors.TextLight)
            Text("Rs ${order.totalPrice.toInt()}", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AppColors.Primary)

            if (order.status == OrderStatus.PENDING) {
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick  = onCancel,
                        modifier = Modifier.weight(1f),
                        shape    = RoundedCornerShape(Radius.SM.dp)
                    ) {
                        Text("Cancel", fontFamily = Roboto, color = AppColors.Error, fontSize = 13.sp)
                    }
                    Button(
                        onClick  = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape    = RoundedCornerShape(Radius.SM.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = AppColors.Success)
                    ) {
                        Text("Confirm", fontFamily = Roboto, color = Color.White, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// ── Order Card Mini (for dashboard preview) ────────────────────────────────────
@Composable
internal fun OrderCardMini(
    order    : VendorOrder,
    modifier : Modifier = Modifier
) {
    Row(
        modifier              = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.MD.dp))
            .background(AppColors.CardBg)
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column {
            Text(order.customerName, fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = AppColors.TextPrimary)
            Text("${order.partName} • Rs ${order.totalPrice.toInt()}", fontFamily = Roboto, fontSize = 11.sp, color = AppColors.TextSecondary)
        }
        VendorOrderStatusBadge(order.status)
    }
}

// ── Order Status Badge (vendor-specific mapping) ───────────────────────────────
@Composable
internal fun VendorOrderStatusBadge(status: OrderStatus) {
    val (color, label) = when (status) {
        OrderStatus.PENDING   -> Pair(AppColors.Warning, "Pending")
        OrderStatus.CONFIRMED -> Pair(AppColors.Primary, "Confirmed")
        OrderStatus.SHIPPED   -> Pair(AppColors.Success, "Shipped")
        OrderStatus.DELIVERED -> Pair(AppColors.Success, "Delivered")
        OrderStatus.CANCELLED -> Pair(AppColors.Error,   "Cancelled")
    }
    DsStatusBadge(label = label, color = color)
}