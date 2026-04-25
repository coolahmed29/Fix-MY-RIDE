package com.example.fix_my_ride.ui.Components


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(28.dp),
                clip = false
            )
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.linearGradient(
                    listOf(GradientStart, GradientMid, GradientEnd)
                )
            )
            .height(220.dp)
    ) {

        // 🔵 Glow effect (top left)
        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(x = (-40).dp, y = (-40).dp)
                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                .blur(40.dp)
        )

        // 🔵 Glow effect (bottom right)
        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 20.dp, y = 20.dp)
                .background(Color.White.copy(alpha = 0.10f), CircleShape)
                .blur(30.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // 🔝 Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fix My Ride",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircleIconButton(Icons.Default.Notifications, "Notifications", onNotifClick)
                    AvatarButton(onAvatarClick)
                }
            }

            // 🔽 Bottom Text
            Column {
                Text(
                    text = greeting,
                    fontFamily = Roboto,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = subtitle,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.White,
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

    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, label = "")

    Card(
        modifier = modifier
            .height(135.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = Color.Black.copy(alpha = 0.10f)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                pressed = true
                card.onClick()
                pressed = false
            },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = DashCardBg.copy(alpha = 0.95f) // glass feel
        ),
        border = BorderStroke(0.6.dp, Color.White.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 18.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔵 Gradient Icon Box
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                card.iconBg,
                                card.iconBg.copy(alpha = 0.7f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    card.icon,
                    contentDescription = card.title,
                    tint = card.iconColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = card.title,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = DashTextPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}
// ── Active Request Card ───────────────────────────────

@Composable
fun ActiveRequestCard(
    serviceName: String = "Roadside Assistance",
    serviceDesc: String = "Mechanic on the way",
    location: String = "Gulberg III, Lahore",
    eta: String = "ETA: 15 min",
    modifier: Modifier = Modifier
) {

    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.97f else 1f, label = "")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = Color.Black.copy(alpha = 0.10f)
            )
            .clickable {
                pressed = true
                pressed = false
            },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = DashCardBg.copy(alpha = 0.95f)
        ),
        border = BorderStroke(0.6.dp, Color.White.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Column(modifier = Modifier.padding(18.dp)) {

            // 🔝 Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge()
                Text(
                    eta,
                    fontFamily = Roboto,
                    fontSize = 12.sp,
                    color = DashTextLight
                )
            }

            Spacer(Modifier.height(16.dp))

            // 🔧 Service Row
            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Primary.copy(alpha = 0.25f),
                                    Primary.copy(alpha = 0.10f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Build,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column {
                    Text(
                        serviceName,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = DashTextPrimary
                    )
                    Text(
                        serviceDesc,
                        fontFamily = Roboto,
                        fontSize = 13.sp,
                        color = DashTextSecondary
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // 📍 Location Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = IconRed,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    location,
                    fontFamily = Roboto,
                    fontSize = 13.sp,
                    color = DashTextSecondary
                )
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