package com.example.fix_my_ride.Dashboards.Vendor.Presentation.viewmodel

// Features/Vendor/Presentation/VendorDashboardViewModel.kt

import androidx.lifecycle.ViewModel
import com.example.fix_my_ride.Dashboards.Vendor.Domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VendorDashboardViewModel : ViewModel() {

    // ── Shop Availability ─────────────────────────────
    private val _isShopOpen = MutableStateFlow(true)
    val isShopOpen: StateFlow<Boolean> = _isShopOpen.asStateFlow()

    fun toggleShopAvailability() {
        _isShopOpen.update { !it }
    }

    // ── Products ──────────────────────────────────────
    private val _products = MutableStateFlow(
        listOf(
            VendorProduct("1", "Brake Pads",      "Brakes",    2500.0, 15, true),
            VendorProduct("2", "Air Filter",      "Engine",    800.0,  30, true),
            VendorProduct("3", "Spark Plugs",     "Engine",    450.0,  50, true),
            VendorProduct("4", "Oil Filter",      "Engine",    600.0,  0,  false),
            VendorProduct("5", "Shock Absorber",  "Suspension",8500.0, 8,  true),
            VendorProduct("6", "Timing Belt",     "Engine",    3200.0, 5,  true)
        )
    )
    val products: StateFlow<List<VendorProduct>> = _products.asStateFlow()

    fun toggleProductAvailability(productId: String) {
        _products.update { list ->
            list.map { p ->
                if (p.id == productId) p.copy(isAvailable = !p.isAvailable)
                else p
            }
        }
    }

    // ── Orders ────────────────────────────────────────
    private val _orders = MutableStateFlow(
        listOf(
            VendorOrder("1", "Ali Hassan",   "Brake Pads",     2, 5000.0,  "2026-04-29", OrderStatus.PENDING),
            VendorOrder("2", "Sara Ahmed",   "Air Filter",     1, 800.0,   "2026-04-28", OrderStatus.CONFIRMED),
            VendorOrder("3", "Usman Khan",   "Spark Plugs",    4, 1800.0,  "2026-04-27", OrderStatus.SHIPPED),
            VendorOrder("4", "Fatima Malik", "Shock Absorber", 1, 8500.0,  "2026-04-26", OrderStatus.DELIVERED),
            VendorOrder("5", "Bilal Raza",   "Timing Belt",    1, 3200.0,  "2026-04-25", OrderStatus.PENDING),
            VendorOrder("6", "Hina Yousuf",  "Oil Filter",     3, 1800.0,  "2026-04-24", OrderStatus.CANCELLED)
        )
    )
    val orders: StateFlow<List<VendorOrder>> = _orders.asStateFlow()

    fun confirmOrder(orderId: String) {
        _orders.update { list ->
            list.map { o ->
                if (o.id == orderId) o.copy(status = OrderStatus.CONFIRMED)
                else o
            }
        }
    }

    fun cancelOrder(orderId: String) {
        _orders.update { list ->
            list.map { o ->
                if (o.id == orderId) o.copy(status = OrderStatus.CANCELLED)
                else o
            }
        }
    }

    // ── Earnings ──────────────────────────────────────
    val earnings = listOf(
        VendorEarning("Jan", 38000.0),
        VendorEarning("Feb", 52000.0),
        VendorEarning("Mar", 41000.0),
        VendorEarning("Apr", 67000.0),
        VendorEarning("May", 48000.0),
        VendorEarning("Jun", 74000.0)
    )

    fun addProduct(name: String, category: String, stock: Int, price: Double) {
        val newProduct = VendorProduct(
            id          = System.currentTimeMillis().toString(),
            name        = name,
            category    = category,
            price       = price,
            stock       = stock,
            isAvailable = stock > 0
        )
        _products.update { it + newProduct }
    }

    val totalEarnings   get() = earnings.sumOf { it.amount }
    val pendingOrders   get() = _orders.value.count { it.status == OrderStatus.PENDING }
    val activeProducts  get() = _products.value.count { it.isAvailable }
    val lowStockCount   get() = _products.value.count { it.stock in 1..5 }
}