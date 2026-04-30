package com.example.fix_my_ride.Dashboards.Workshop.Presentation.viewmodel

// Presentation/WorkshopDashboardViewModel.kt

import androidx.lifecycle.ViewModel
import com.example.fix_my_ride.Dashboards.Workshop.Domain.model.WorkshopPackage
import com.example.fix_my_ride.Dashboards.Workshop.Domain.model.RequestStatus
import com.example.fix_my_ride.Dashboards.Workshop.Domain.model.WorkshopEarning
import com.example.fix_my_ride.Dashboards.Workshop.Domain.model.WorkshopRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WorkshopDashboardViewModel : ViewModel() {

    // ── Workshop Availability ─────────────────────────
    private val _isWorkshopOpen = MutableStateFlow(true)
    val isWorkshopOpen: StateFlow<Boolean> = _isWorkshopOpen.asStateFlow()

    fun toggleWorkshopAvailability() {
        _isWorkshopOpen.update { !it }
    }

    // ── Packages ──────────────────────────────────────
    private val _packages = MutableStateFlow(
        listOf(
            WorkshopPackage("1", "Basic Wash", "Exterior wash + vacuum", 1500.0, "1 hour", true),
            WorkshopPackage("2", "Full Detail",      "Interior + exterior detailing",   4500.0, "3-4 hours",true),
            WorkshopPackage("3", "Paint Protection", "Ceramic coating + polish",        9500.0, "6-8 hours",false),
            WorkshopPackage("4", "Engine Clean",     "Engine bay cleaning",             2500.0, "2 hours",  true)
        )
    )
    val packages: StateFlow<List<WorkshopPackage>> = _packages.asStateFlow()

    fun togglePackageAvailability(packageId: String) {
        _packages.update { list ->
            list.map { pkg ->
                if (pkg.id == packageId) pkg.copy(isAvailable = !pkg.isAvailable)
                else pkg
            }
        }
    }

    // ── Requests ──────────────────────────────────────
    private val _requests = MutableStateFlow(
        listOf(
            WorkshopRequest("1", "Ali Hassan",   "Basic Wash",       "2026-05-01", "10:00 AM", 1500.0, RequestStatus.PENDING),
            WorkshopRequest("2", "Sara Ahmed",   "Full Detail",      "2026-05-02", "12:00 PM", 4500.0, RequestStatus.PENDING),
            WorkshopRequest("3", "Usman Khan",   "Engine Clean",     "2026-05-03", "09:00 AM", 2500.0, RequestStatus.ACCEPTED),
            WorkshopRequest("4", "Fatima Malik", "Paint Protection", "2026-05-04", "02:00 PM", 9500.0, RequestStatus.REJECTED),
            WorkshopRequest("5", "Bilal Raza",   "Basic Wash",       "2026-05-05", "11:00 AM", 1500.0, RequestStatus.PENDING)
        )
    )
    val requests: StateFlow<List<WorkshopRequest>> = _requests.asStateFlow()

    fun acceptRequest(requestId: String) {
        _requests.update { list ->
            list.map { req ->
                if (req.id == requestId) req.copy(status = RequestStatus.ACCEPTED)
                else req
            }
        }
    }

    fun rejectRequest(requestId: String) {
        _requests.update { list ->
            list.map { req ->
                if (req.id == requestId) req.copy(status = RequestStatus.REJECTED)
                else req
            }
        }
    }

    // ── Earnings ──────────────────────────────────────
    val earnings = listOf(
        WorkshopEarning("Jan", 45000.0),
        WorkshopEarning("Feb", 62000.0),
        WorkshopEarning("Mar", 38000.0),
        WorkshopEarning("Apr", 71000.0),
        WorkshopEarning("May", 55000.0),
        WorkshopEarning("Jun", 83000.0)
    )

    val totalEarnings   get() = earnings.sumOf { it.amount }
    val pendingRequests get() = _requests.value.count { it.status == RequestStatus.PENDING }
    val activePackages  get() = _packages.value.count { it.isAvailable }
}