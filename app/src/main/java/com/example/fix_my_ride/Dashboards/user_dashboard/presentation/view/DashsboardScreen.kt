package com.example.fix_my_ride.Dashboards.user_dashboard.presentation.view

// features/dashboard/presentation/DashboardScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.ui.Components.ActiveRequestCard
import com.example.fix_my_ride.ui.Components.BottomNavItem
import com.example.fix_my_ride.ui.Components.DashboardBottomNav
import com.example.fix_my_ride.ui.Components.FeatureCard
import com.example.fix_my_ride.ui.Components.FeatureCardsGrid
import com.example.fix_my_ride.ui.Components.HeroSection
import com.example.fix_my_ride.ui.theme.*

@Composable
fun DashboardScreen() {

    var selectedNav by remember { mutableIntStateOf(0) }

    val featureCards = listOf(
        FeatureCard(
            "Roadside Help",
            Icons.Default.Warning,
            IconYellow,
            IconYellow.copy(alpha = 0.15f)
        ),
        FeatureCard("Spare Parts",   Icons.Default.Build,        IconRed,    IconRed.copy(alpha = 0.15f)),
        FeatureCard("AI Assistant",  Icons.Default.SmartToy,     IconGreen,  IconGreen.copy(alpha = 0.15f)),
        FeatureCard("Tow Service",   Icons.Default.DirectionsCar, IconOrange, IconOrange.copy(alpha = 0.15f))
    )

    val navItems = listOf(
        BottomNavItem(Icons.Default.Home, "Home"),
        BottomNavItem(Icons.Default.LocationOn, "Track"),
        BottomNavItem(Icons.Default.Person,     "Profile"),
        BottomNavItem(Icons.Default.Settings,   "Settings")
    )

    Scaffold(
        containerColor = DashBackground,
        bottomBar = {
            DashboardBottomNav(
                navItems    = navItems,
                selectedNav = selectedNav,
                onNavSelect = { selectedNav = it }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier       = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            item { HeroSection() }

            item {
                SectionTitle("Quick Actions", Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
            }

            item {
                FeatureCardsGrid(featureCards, Modifier.padding(horizontal = 16.dp))
            }

            item {
                SectionTitle("Active Request", Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp))
                ActiveRequestCard(modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text       = title,
        fontFamily = Montserrat,
        fontWeight = FontWeight.Bold,
        fontSize   = 18.sp,
        color      = DashTextPrimary,
        modifier   = modifier
    )
}