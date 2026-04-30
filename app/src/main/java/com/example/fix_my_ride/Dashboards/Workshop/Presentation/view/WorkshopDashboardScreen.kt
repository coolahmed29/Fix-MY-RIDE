package com.example.fix_my_ride.Dashboards.Workshop.Presentation.view

// Presentation/View/WorkshopDashboardScreen.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fix_my_ride.Dashboards.Workshop.Domain.model.RequestStatus
import com.example.fix_my_ride.Dashboards.Workshop.Domain.model.WorkshopEarning
import com.example.fix_my_ride.Dashboards.Workshop.Domain.model.WorkshopPackage
import com.example.fix_my_ride.Dashboards.Workshop.Domain.model.WorkshopRequest
import com.example.fix_my_ride.Dashboards.Workshop.Presentation.viewmodel.WorkshopDashboardViewModel
import com.example.fix_my_ride.ui.theme.*

@Composable
fun WorkshopDashboardScreen(
    onNavigateToPackages : () -> Unit = {},
    onNavigateToRequests : () -> Unit = {},
    onNavigateToEarnings : () -> Unit = {},
    viewModel            : WorkshopDashboardViewModel = viewModel()
) {
    val isOpen          by viewModel.isWorkshopOpen.collectAsStateWithLifecycle()
    val packages        by viewModel.packages.collectAsStateWithLifecycle()
    val requests        by viewModel.requests.collectAsStateWithLifecycle()
    val pendingCount    = requests.count { it.status == RequestStatus.PENDING }

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = DashBackground,
        bottomBar = {
            NavigationBar(containerColor = DashCardBg) {
                listOf(
                    Triple(Icons.Default.Dashboard,  "Dashboard", 0),
                    Triple(Icons.Default.Inventory,  "Packages",  1),
                    Triple(Icons.Default.Assignment, "Requests",  2),
                    Triple(Icons.Default.BarChart,   "Earnings",  3)
                ).forEach { (icon, label, index) ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick  = { selectedTab = index },
                        icon     = {
                            BadgedBox(
                                badge = {
                                    if (index == 2 && pendingCount > 0)
                                        Badge { Text(pendingCount.toString()) }
                                }
                            ) {
                                Icon(icon, contentDescription = label)
                            }
                        },
                        label    = { Text(label, fontFamily = Roboto, fontSize = 11.sp) }
                    )
                }
            }
        }
    ) { padding ->

        when (selectedTab) {
            0 -> DashboardTab(
                isOpen    = isOpen,
                onToggle  = viewModel::toggleWorkshopAvailability,
                packages  = packages,
                requests  = requests,
                earnings  = viewModel.earnings,
                total     = viewModel.totalEarnings,
                modifier  = Modifier.padding(padding)
            )
            1 -> PackagesTab(
                packages = packages,
                onToggle = viewModel::togglePackageAvailability,
                modifier = Modifier.padding(padding)
            )
            2 -> RequestsTab(
                requests = requests,
                onAccept = viewModel::acceptRequest,
                onReject = viewModel::rejectRequest,
                modifier = Modifier.padding(padding)
            )
            3 -> EarningsTab(
                earnings = viewModel.earnings,
                total    = viewModel.totalEarnings,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

// ── Tab 1: Dashboard ──────────────────────────────────────────────────────────
@Composable
private fun DashboardTab(
    isOpen   : Boolean,
    onToggle : () -> Unit,
    packages : List<WorkshopPackage>,
    requests : List<WorkshopRequest>,
    earnings : List<WorkshopEarning>,
    total    : Double,
    modifier : Modifier = Modifier
) {
    LazyColumn(
        modifier       = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Primary, Primary.copy(alpha = 0.7f))
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text       = "Total Earnings",
                                fontFamily = Roboto,
                                fontSize   = 13.sp,
                                color      = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text       = "Rs ${"%,.0f".format(total)}",
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 28.sp,
                                color      = Color.White
                            )
                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint     = Color.White.copy(alpha = 0.9f),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text       = "+15% from last month",
                                    fontFamily = Roboto,
                                    fontSize   = 11.sp,
                                    color      = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }

                        // Mini Bar Chart
                        Row(
                            verticalAlignment     = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            val max = earnings.maxOf { it.amount }
                            earnings.takeLast(5).forEach { e ->
                                val heightFraction = (e.amount / max).toFloat()
                                Box(
                                    modifier = Modifier
                                        .width(8.dp)
                                        .height((50 * heightFraction).dp)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(Color.White.copy(alpha = 0.6f))
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // Workshop Open/Close Toggle
                    Row(
                        modifier              = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (isOpen) IconGreen else Color.Gray)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text       = if (isOpen) "Workshop Open" else "Workshop Closed",
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
                                checkedTrackColor   = IconGreen,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }

        // Stats Row
        item {
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    icon    = Icons.Default.Inventory,
                    label   = "Packages",
                    value   = packages.count { it.isAvailable }.toString(),
                    sub     = "active",
                    color   = Primary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon    = Icons.Default.Pending,
                    label   = "Requests",
                    value   = requests.count { it.status == RequestStatus.PENDING }.toString(),
                    sub     = "pending",
                    color   = IconYellow,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon    = Icons.Default.CheckCircle,
                    label   = "Done",
                    value   = requests.count { it.status == RequestStatus.ACCEPTED }.toString(),
                    sub     = "accepted",
                    color   = IconGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Recent Requests
        item {
            Spacer(Modifier.height(20.dp))
            Text(
                text       = "Recent Requests",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize   = 16.sp,
                color      = DashTextPrimary,
                modifier   = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(10.dp))
        }

        val recent = requests.take(3)
        items(recent.size) { i ->
            RequestCardMini(
                request  = recent[i],
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}

// ── Tab 2: Packages ───────────────────────────────────────────────────────────
@Composable
private fun PackagesTab(
    packages : List<WorkshopPackage>,
    onToggle : (String) -> Unit,
    modifier : Modifier = Modifier
) {
    LazyColumn(
        modifier       = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text       = "My Packages",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize   = 20.sp,
                color      = DashTextPrimary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text       = "${packages.count { it.isAvailable }} active packages",
                fontFamily = Roboto,
                fontSize   = 13.sp,
                color      = DashTextSecondary
            )
            Spacer(Modifier.height(8.dp))
        }

        items(packages.size) { i ->
            PackageCard(
                pkg      = packages[i],
                onToggle = { onToggle(packages[i].id) }
            )
        }
    }
}

// ── Tab 3: Requests ───────────────────────────────────────────────────────────
@Composable
private fun RequestsTab(
    requests : List<WorkshopRequest>,
    onAccept : (String) -> Unit,
    onReject : (String) -> Unit,
    modifier : Modifier = Modifier
) {
    val pending  = requests.filter { it.status == RequestStatus.PENDING }
    val others   = requests.filter { it.status != RequestStatus.PENDING }

    LazyColumn(
        modifier        = modifier.fillMaxSize(),
        contentPadding  = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text       = "Booking Requests",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize   = 20.sp,
                color      = DashTextPrimary
            )
            Spacer(Modifier.height(12.dp))
            if (pending.isNotEmpty()) {
                Text(
                    text       = "Pending (${pending.size})",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = IconYellow
                )
                Spacer(Modifier.height(8.dp))
            }
        }

        items(pending.size) { i ->
            RequestCard(
                request  = pending[i],
                onAccept = { onAccept(pending[i].id) },
                onReject = { onReject(pending[i].id) }
            )
        }

        if (others.isNotEmpty()) {
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    text       = "Previous",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = DashTextSecondary
                )
                Spacer(Modifier.height(8.dp))
            }
            items(others.size) { i ->
                RequestCard(
                    request  = others[i],
                    onAccept = {},
                    onReject = {}
                )
            }
        }
    }
}

// ── Tab 4: Earnings ───────────────────────────────────────────────────────────
@Composable
private fun EarningsTab(
    earnings : List<WorkshopEarning>,
    total    : Double,
    modifier : Modifier = Modifier
) {
    val maxEarning = earnings.maxOf { it.amount }

    LazyColumn(
        modifier       = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text       = "Earnings",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize   = 20.sp,
                color      = DashTextPrimary
            )
        }

        // Total card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(Primary, Primary.copy(alpha = 0.75f))))
                    .padding(24.dp)
            ) {
                Column {
                    Text("Total Earnings", fontFamily = Roboto, fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text       = "Rs ${"%,.0f".format(total)}",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 26.sp,
                        color      = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TrendingUp, null, tint = Color.White.copy(0.9f), modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("+15% from last month", fontFamily = Roboto, fontSize = 11.sp, color = Color.White.copy(0.9f))
                    }
                }
            }
        }

        // Bar Chart
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(DashCardBg)
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text("Monthly Earnings", fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DashTextPrimary)
                        Text("2026", fontFamily = Roboto, fontSize = 12.sp, color = DashTextSecondary)
                    }
                    Spacer(Modifier.height(20.dp))

                    // Bars
                    Row(
                        modifier              = Modifier.fillMaxWidth().height(120.dp),
                        verticalAlignment     = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        earnings.forEach { e ->
                            val fraction = (e.amount / maxEarning).toFloat()
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier            = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .height((100 * fraction).dp)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Primary, Primary.copy(alpha = 0.4f))
                                            )
                                        )
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(e.month, fontFamily = Roboto, fontSize = 10.sp, color = DashTextLight)
                            }
                        }
                    }
                }
            }
        }

        // Monthly breakdown
        item {
            Text("Monthly Breakdown", fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DashTextPrimary)
        }

        items(earnings.size) { i ->
            val e = earnings[i]
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DashCardBg)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(e.month, fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DashTextPrimary)
                Text("Rs ${"%,.0f".format(e.amount)}", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Primary)
            }
        }
    }
}

// ── Reusable Components ───────────────────────────────────────────────────────
@Composable
private fun StatCard(
    icon     : ImageVector,
    label    : String,
    value    : String,
    sub      : String,
    color    : Color,
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(DashCardBg)
            .padding(14.dp)
    ) {
        Column {
            Box(
                modifier         = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.height(10.dp))
            Text(value, fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DashTextPrimary)
            Text(label, fontFamily = Roboto, fontSize = 11.sp, color = DashTextSecondary)
            Text(sub,   fontFamily = Roboto, fontSize = 10.sp, color = color)
        }
    }
}

@Composable
private fun PackageCard(
    pkg      : WorkshopPackage,
    onToggle : () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DashCardBg)
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(pkg.name, fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = DashTextPrimary)
                    Text(pkg.description, fontFamily = Roboto, fontSize = 12.sp, color = DashTextSecondary)
                }
                Switch(
                    checked         = pkg.isAvailable,
                    onCheckedChange = { onToggle() },
                    colors          = SwitchDefaults.colors(
                        checkedTrackColor   = IconGreen,
                        uncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
                    )
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Chip(label = "Rs ${pkg.price.toInt()}", color = Primary)
                Chip(label = pkg.duration,              color = IconYellow)
                Chip(
                    label = if (pkg.isAvailable) "Available" else "Unavailable",
                    color = if (pkg.isAvailable) IconGreen else Color.Gray
                )
            }
        }
    }
}

@Composable
private fun RequestCard(
    request  : WorkshopRequest,
    onAccept : () -> Unit,
    onReject : () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DashCardBg)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(request.customerName, fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DashTextPrimary)
                StatusBadge(request.status)
            }
            Text(request.packageName, fontFamily = Roboto, fontSize = 13.sp, color = DashTextSecondary)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("📅 ${request.date}", fontFamily = Roboto, fontSize = 12.sp, color = DashTextLight)
                Text("🕐 ${request.timeSlot}", fontFamily = Roboto, fontSize = 12.sp, color = DashTextLight)
            }
            Text("Rs ${request.price.toInt()}", fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Primary)

            if (request.status == RequestStatus.PENDING) {
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick  = onReject,
                        modifier = Modifier.weight(1f),
                        shape    = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reject", fontFamily = Roboto, color = IconRed, fontSize = 13.sp)
                    }
                    Button(
                        onClick  = onAccept,
                        modifier = Modifier.weight(1f),
                        shape    = RoundedCornerShape(10.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = IconGreen)
                    ) {
                        Text("Accept", fontFamily = Roboto, color = Color.White, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun RequestCardMini(
    request  : WorkshopRequest,
    modifier : Modifier = Modifier
) {
    Row(
        modifier              = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DashCardBg)
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column {
            Text(request.customerName, fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = DashTextPrimary)
            Text("${request.packageName} • ${request.timeSlot}", fontFamily = Roboto, fontSize = 11.sp, color = DashTextSecondary)
        }
        StatusBadge(request.status)
    }
}

@Composable
private fun StatusBadge(status: RequestStatus) {
    val (color, label) = when (status) {
        RequestStatus.PENDING  -> Pair(IconYellow, "Pending")
        RequestStatus.ACCEPTED -> Pair(IconGreen,  "Accepted")
        RequestStatus.REJECTED -> Pair(IconRed,    "Rejected")
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(label, fontFamily = Roboto, fontWeight = FontWeight.Medium, fontSize = 11.sp, color = color)
    }
}

@Composable
private fun Chip(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(label, fontFamily = Roboto, fontSize = 11.sp, color = color, fontWeight = FontWeight.Medium)
    }
}