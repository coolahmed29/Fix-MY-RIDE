package com.example.fix_my_ride.Dashboards.Vendor.Presentation.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fix_my_ride.Dashboards.Vendor.Presentation.viewmodel.VendorDashboardViewModel
import com.example.fix_my_ride.ui.Components.Dashboard.AddProductDialog
import com.example.fix_my_ride.ui.Components.Dashboard.AppColors
import com.example.fix_my_ride.ui.Components.Dashboard.EmptyStateOnboarding
import com.example.fix_my_ride.ui.Components.Dashboard.OrdersTab
import com.example.fix_my_ride.ui.Components.Dashboard.ProductsTab
import com.example.fix_my_ride.ui.Components.Dashboard.VendorDashboardTab
import com.example.fix_my_ride.ui.Components.Dashboard.VendorEarningsTab
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
fun VendorDashboardScreen(
    viewModel: VendorDashboardViewModel = viewModel()
) {
    val isOpen   by viewModel.isShopOpen.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val orders   by viewModel.orders.collectAsStateWithLifecycle()

    var selectedTab   by remember { mutableIntStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddProductDialog(
            onDismiss = { showAddDialog = false },
            onSave    = { name, category, stock, price ->
                viewModel.addProduct(name, category, stock, price)
                showAddDialog = false
            }
        )
    }

    Scaffold(
        containerColor = AppColors.Background,
        floatingActionButton = {
            if (selectedTab == 1) {
                FloatingActionButton(
                    onClick        = { showAddDialog = true },
                    containerColor = AppColors.Primary,
                    shape          = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Product", tint = Color.White)
                }
            }
        },
        bottomBar = {
            VendorBottomBar(
                selectedTab   = selectedTab,
                pendingOrders = viewModel.pendingOrders,
                onTabChange   = { selectedTab = it }
            )
        }
    ) { padding ->
        when (selectedTab) {
            0 -> if (products.isEmpty() && orders.isEmpty()) {
                EmptyStateOnboarding(
                    modifier   = Modifier.padding(padding),
                    onAddClick = { selectedTab = 1; showAddDialog = true }
                )
            } else {
                VendorDashboardTab(
                    isOpen      = isOpen,
                    onToggle    = viewModel::toggleShopAvailability,
                    products    = products,
                    orders      = orders,
                    earnings    = viewModel.earnings,
                    total       = viewModel.totalEarnings,
                    lowStock    = viewModel.lowStockCount,
                    onTabChange = { selectedTab = it },
                    modifier    = Modifier.padding(padding)
                )
            }
            1 -> ProductsTab(products = products, onToggle = viewModel::toggleProductAvailability, modifier = Modifier.padding(padding))
            2 -> OrdersTab(orders = orders, onConfirm = viewModel::confirmOrder, onCancel = viewModel::cancelOrder, modifier = Modifier.padding(padding))
            3 -> VendorEarningsTab(earnings = viewModel.earnings, total = viewModel.totalEarnings, modifier = Modifier.padding(padding))
        }
    }
}

// ── Bottom Nav — extracted ─────────────────────────────────────────────────────
@Composable
private fun VendorBottomBar(
    selectedTab   : Int,
    pendingOrders : Int,
    onTabChange   : (Int) -> Unit
) {
    NavigationBar(containerColor = AppColors.CardBg) {
        listOf(
            Triple(Icons.Default.Dashboard,   "Dashboard", 0),
            Triple(Icons.Default.Inventory2,  "Products",  1),
            Triple(Icons.Default.ShoppingBag, "Orders",    2),
            Triple(Icons.Default.BarChart,    "Earnings",  3)
        ).forEach { (icon, label, index) ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick  = { onTabChange(index) },
                icon = {
                    BadgedBox(badge = {
                        if (index == 2 && pendingOrders > 0)
                            Badge { Text(pendingOrders.toString()) }
                    }) { Icon(icon, contentDescription = label) }
                },
                label = { Text(label, fontFamily = Roboto, fontSize = 11.sp) }
            )
        }
    }
}