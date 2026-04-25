package com.example.fix_my_ride.ui.Components

// ui/Components/PartCard.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Part
import com.example.fix_my_ride.ui.theme.DashCardBg
import com.example.fix_my_ride.ui.theme.DashTextLight
import com.example.fix_my_ride.ui.theme.DashTextPrimary
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.IconYellow
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
fun PartCard(
    part     : Part,
    onClick  : () -> Unit,
    modifier : Modifier = Modifier
) {
    Card(
        modifier  = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 4.dp,
                shape        = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor    = Color.Black.copy(alpha = 0.04f)
            )
            .clickable { onClick() },
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = DashCardBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Part Icon Box ─────────────────────────
            Box(
                modifier         = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Primary.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.Build,
                    contentDescription = null,
                    tint               = Primary,
                    modifier           = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // ── Part Info ─────────────────────────────
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = part.name,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = DashTextPrimary,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text       = part.carModel,
                    fontFamily = Roboto,
                    fontSize   = 12.sp,
                    color      = DashTextSecondary,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // ── Rating + Vendors ──────────────────
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector        = Icons.Default.Star,
                            contentDescription = null,
                            tint               = IconYellow,
                            modifier           = Modifier.size(12.dp)
                        )
                        Text(
                            text       = part.rating.toString(),
                            fontFamily = Roboto,
                            fontSize   = 11.sp,
                            color      = DashTextSecondary
                        )
                    }

                    // Dot separator
                    Text(
                        text  = "•",
                        color = DashTextLight,
                        fontSize = 10.sp
                    )

                    // Vendor count
                    Text(
                        text       = "${part.vendorCount} vendors",
                        fontFamily = Roboto,
                        fontSize   = 11.sp,
                        color      = DashTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // ── Price ─────────────────────────────────
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text       = "Rs ${part.price.toInt()}",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    color      = Primary
                )
                Text(
                    text       = part.category,
                    fontFamily = Roboto,
                    fontSize   = 10.sp,
                    color      = DashTextLight
                )
            }
        }
    }
}