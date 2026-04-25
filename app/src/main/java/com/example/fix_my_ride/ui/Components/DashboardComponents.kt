package com.example.fix_my_ride.ui.Components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.ui.theme.*

// ── Data Models ───────────────────────────────────────

data class FeatureCard(
    val title    : String,
    val icon     : ImageVector,
    val iconColor: Color,
    val iconBg   : Color,
    val onClick  : () -> Unit = {}
)

data class BottomNavItem(
    val icon : ImageVector,
    val label: String
)

// ── HeroSection ───────────────────────────────────────

@Composable
fun HeroSection(
    greeting     : String = "Good Morning! 👋",
    subtitle     : String = "How can we help\nyou today?",
    onNotifClick : () -> Unit = {},
    onAvatarClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                Brush.linearGradient(listOf(GradientStart, GradientMid, GradientEnd))
            )
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(x = (-40).dp, y = (-40).dp)
                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                .blur(40.dp)
        )
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomEnd)
                .background(Color.White.copy(alpha = 0.10f), CircleShape)
                .blur(30.dp)
        )

        Column(
            modifier            = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text       = "Fix My Ride",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 20.sp,
                    color      = Color.White
                )
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircleIconButton(Icons.Default.Notifications, "Notifications", onNotifClick)
                    AvatarButton(onAvatarClick)
                }
            }

            Column {
                Text(
                    text       = greeting,
                    fontFamily = Roboto,
                    fontSize   = 14.sp,
                    color      = Color.White.copy(alpha = 0.85f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text       = subtitle,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 24.sp,
                    color      = Color.White,
                    lineHeight = 32.sp
                )
            }
        }
    }
}

@Composable
private fun CircleIconButton(icon: ImageVector, description: String, onClick: () -> Unit) {
    Box(
        modifier         = Modifier
            .size(38.dp)
            .background(Color.White.copy(alpha = 0.2f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, description, tint = Color.White, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun AvatarButton(onClick: () -> Unit) {
    Box(
        modifier         = Modifier
            .size(38.dp)
            .shadow(4.dp, CircleShape)
            .background(Color.White, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Person, "Profile", tint = GradientStart, modifier = Modifier.size(22.dp))
    }
}

// ── Feature Cards Grid ────────────────────────────────

@Composable
fun FeatureCardsGrid(cards: List<FeatureCard>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        cards.chunked(2).forEach { rowCards ->
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowCards.forEach { card ->
                    FeatureCardItem(card = card, modifier = Modifier.weight(1f))
                }
                if (rowCards.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

// ── Single Feature Card ───────────────────────────────

@Composable
fun FeatureCardItem(card: FeatureCard, modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier
            .height(120.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp), ambientColor = Color.Black.copy(alpha = 0.05f))
            .clickable { card.onClick() },
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = DashCardBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier            = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier         = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(card.iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(card.icon, card.title, tint = card.iconColor, modifier = Modifier.size(24.dp))
            }
            Text(card.title, fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = DashTextPrimary)
        }
    }
}

// ── Active Request Card ───────────────────────────────

@Composable
fun ActiveRequestCard(
    serviceName: String   = "Roadside Assistance",
    serviceDesc: String   = "Mechanic on the way",
    location   : String   = "Gulberg III, Lahore",
    eta        : String   = "ETA: 15 min",
    modifier   : Modifier = Modifier
) {
    Card(
        modifier  = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), ambientColor = Color.Black.copy(alpha = 0.05f)),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = DashCardBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                StatusBadge()
                Text(eta, fontFamily = Roboto, fontSize = 12.sp, color = DashTextLight)
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier         = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Primary.copy(alpha = 0.10f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Build, null, tint = Primary, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(serviceName, fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = DashTextPrimary)
                    Text(serviceDesc, fontFamily = Roboto, fontSize = 13.sp, color = DashTextSecondary)
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, tint = IconRed, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(location, fontFamily = Roboto, fontSize = 13.sp, color = DashTextSecondary)
            }
        }
    }
}

// ── Status Badge ──────────────────────────────────────

@Composable
fun StatusBadge(
    label   : String   = "● Active",
    color   : Color    = IconGreen,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(label, fontFamily = Roboto, fontWeight = FontWeight.Medium, fontSize = 12.sp, color = color)
    }
}

// ── Bottom Navigation Bar ─────────────────────────────

@Composable
fun DashboardBottomNav(
    navItems   : List<BottomNavItem>,
    selectedNav: Int,
    onNavSelect: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(30.dp), ambientColor = Color.Black.copy(alpha = 0.10f))
                .clip(RoundedCornerShape(30.dp)),
            containerColor = DashCardBg,
            tonalElevation = 0.dp
        ) {
            navItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedNav == index,
                    onClick  = { onNavSelect(index) },
                    icon     = { Icon(item.icon, item.label, modifier = Modifier.size(22.dp)) },
                    colors   = NavigationBarItemDefaults.colors(
                        selectedIconColor   = Primary,
                        unselectedIconColor = DashTextLight,
                        indicatorColor      = Primary.copy(alpha = 0.10f)
                    )
                )
            }
        }
    }
}