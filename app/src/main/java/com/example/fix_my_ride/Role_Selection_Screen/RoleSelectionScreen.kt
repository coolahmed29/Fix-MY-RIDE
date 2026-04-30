package com.example.fix_my_ride.Role_Selection_Screen

// Presentation/View/RoleSelectionScreen.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.ui.theme.*

enum class UserRole {
    CAR_OWNER, WORKSHOP_OWNER, VENDOR, MECHANIC
}

@Composable
fun RoleSelectionScreen(
    onContinue : (UserRole) -> Unit
) {
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }

    val roles = listOf(
        Triple(UserRole.CAR_OWNER,      Icons.Default.DirectionsCar, "Car Owner"),
        Triple(UserRole.WORKSHOP_OWNER, Icons.Default.Store,         "Workshop Owner"),
        Triple(UserRole.VENDOR,         Icons.Default.Inventory,     "Vendor"),
        Triple(UserRole.MECHANIC,       Icons.Default.Build,         "Mechanic")
    )

    Column(
        modifier            = Modifier
            .fillMaxSize()
            .background(DashBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ── Title ─────────────────────────────────────
        Text(
            text       = "Please select your role",
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize   = 26.sp,
            color      = DashTextPrimary,
            textAlign  = TextAlign.Center
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text       = "Select the role that best describes you.\nYou can change this later from settings.",
            fontFamily = Roboto,
            fontSize   = 14.sp,
            color      = DashTextSecondary,
            textAlign  = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(48.dp))

        // ── Role Cards — 2x2 Grid ─────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            roles.chunked(2).forEach { rowRoles ->
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowRoles.forEach { (role, icon, label) ->
                        RoleCard(
                            icon       = icon,
                            label      = label,
                            isSelected = selectedRole == role,
                            onClick    = { selectedRole = role },
                            modifier   = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(48.dp))

        // ── Continue Button ───────────────────────────
        Button(
            onClick  = { selectedRole?.let { onContinue(it) } },
            enabled  = selectedRole != null,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(52.dp),
            shape  = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor         = Primary,
                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text       = "Continue",
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 16.sp,
                color      = Color.White
            )
        }
    }
}

// ── Role Card ─────────────────────────────────────────
@Composable
private fun RoleCard(
    icon       : ImageVector,
    label      : String,
    isSelected : Boolean,
    onClick    : () -> Unit,
    modifier   : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) Primary.copy(alpha = 0.08f)
                else DashCardBg
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Primary else Color.Gray.copy(alpha = 0.15f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = label,
                tint               = if (isSelected) Primary else Color.Gray.copy(alpha = 0.5f),
                modifier           = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text       = label,
                fontFamily = Montserrat,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize   = 13.sp,
                color      = if (isSelected) Primary else DashTextSecondary,
                textAlign  = TextAlign.Center
            )
        }
    }
}