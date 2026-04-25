package com.example.fix_my_ride.ui.Components

// ui/Components/VendorCard.kt

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Vendor
import com.example.fix_my_ride.ui.theme.DashCardBg
import com.example.fix_my_ride.ui.theme.DashTextLight
import com.example.fix_my_ride.ui.theme.DashTextPrimary
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.IconGreen
import com.example.fix_my_ride.ui.theme.IconRed
import com.example.fix_my_ride.ui.theme.IconYellow
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
fun VendorCard(
    vendor    : Vendor,
    onBuyNow  : () -> Unit,
    onContact : () -> Unit,
    modifier  : Modifier = Modifier
) {
    Card(
        modifier  = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 4.dp,
                shape        = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor    = Color.Black.copy(alpha = 0.04f)
            ),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = DashCardBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // ── Top Row — Name + Stock ─────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Vendor Name + Icon
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier         = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Primary.copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector        = Icons.Default.Store,
                            contentDescription = null,
                            tint               = Primary,
                            modifier           = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text       = vendor.name,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp,
                            color      = DashTextPrimary,
                            maxLines   = 1,
                            overflow   = TextOverflow.Ellipsis
                        )
                        Text(
                            text       = vendor.address,
                            fontFamily = Roboto,
                            fontSize   = 11.sp,
                            color      = DashTextLight,
                            maxLines   = 1,
                            overflow   = TextOverflow.Ellipsis
                        )
                    }
                }

                // ── Stock Badge ───────────────────────
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (vendor.inStock)
                                IconGreen.copy(alpha = 0.12f)
                            else
                                IconRed.copy(alpha = 0.12f)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text       = if (vendor.inStock)
                            "In Stock"
                        else
                            "Out of Stock",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium,
                        fontSize   = 11.sp,
                        color      = if (vendor.inStock) IconGreen else IconRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Middle Row — Price + Rating + Distance ─
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Price
                Column {
                    Text(
                        text       = "Price",
                        fontFamily = Roboto,
                        fontSize   = 11.sp,
                        color      = DashTextLight
                    )
                    Text(
                        text       = "Rs ${vendor.price.toInt()}",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp,
                        color      = Primary
                    )
                }

                // Rating
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text       = "Rating",
                        fontFamily = Roboto,
                        fontSize   = 11.sp,
                        color      = DashTextLight
                    )
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector        = Icons.Default.Star,
                            contentDescription = null,
                            tint               = IconYellow,
                            modifier           = Modifier.size(14.dp)
                        )
                        Text(
                            text       = vendor.rating.toString(),
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp,
                            color      = DashTextPrimary
                        )
                    }
                }

                // Distance
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text       = "Distance",
                        fontFamily = Roboto,
                        fontSize   = 11.sp,
                        color      = DashTextLight
                    )
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector        = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint               = IconRed,
                            modifier           = Modifier.size(14.dp)
                        )
                        Text(
                            text       = "${vendor.distance} km",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp,
                            color      = DashTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Action Buttons ────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Contact Button
                OutlinedButton(
                    onClick  = onContact,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(12.dp),
                    enabled  = vendor.inStock
                ) {
                    Icon(
                        imageVector        = Icons.Default.Phone,
                        contentDescription = null,
                        modifier           = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text       = "Contact",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium,
                        fontSize   = 13.sp
                    )
                }

                // Buy Now Button
                Button(
                    onClick  = onBuyNow,
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(12.dp),
                    enabled  = vendor.inStock,
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = Primary,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text       = if (vendor.inStock) "Buy Now" else "Unavailable",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium,
                        fontSize   = 13.sp,
                        color      = Color.White
                    )
                }
            }
        }
    }
}