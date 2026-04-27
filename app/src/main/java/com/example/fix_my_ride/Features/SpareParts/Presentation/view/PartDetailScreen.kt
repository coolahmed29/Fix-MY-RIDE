package com.example.fix_my_ride.Features.SpareParts.Presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Part
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Vendor
import com.example.fix_my_ride.Features.SpareParts.Presentation.viewmodel.SparePartsViewModel
import com.example.fix_my_ride.ui.Components.EmptyState
import com.example.fix_my_ride.ui.Components.VendorCard
import com.example.fix_my_ride.ui.theme.DashBackground
import com.example.fix_my_ride.ui.theme.DashCardBg
import com.example.fix_my_ride.ui.theme.DashTextLight
import com.example.fix_my_ride.ui.theme.DashTextPrimary
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
fun PartDetailScreen(
    part        : Part,
    onBack      : () -> Unit,
    onChatClick : (vendorId: String, vendorName: String) -> Unit,  // ✅ lambda
    viewModel   : SparePartsViewModel = hiltViewModel()
) {
    val vendorsState by viewModel.vendorsState
        .collectAsStateWithLifecycle()

    LaunchedEffect(part.id) {
        viewModel.onPartSelect(part)
    }

    Scaffold(
        containerColor = DashBackground
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding      = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            item(key = "header") {
                PartDetailHeader(part = part, onBack = onBack)
            }

            item(key = "part_info") {
                PartInfoCard(
                    part     = part,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            // ✅ onChatClick pass karo
            vendorsSection(
                vendorsState = vendorsState,
                onChatClick  = onChatClick
            )
        }
    }
}

// ── Vendors Section ───────────────────────────────────
private fun LazyListScope.vendorsSection(
    vendorsState : AuthResult<*>?,
    onChatClick  : (vendorId: String, vendorName: String) -> Unit  // ✅ lambda
) {
    item(key = "vendors_header") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text       = "Available Vendors",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp,
                color      = DashTextPrimary
            )

            if (vendorsState is AuthResult.Success<*>) {
                val count = (vendorsState.data as? List<*>)?.size ?: 0
                if (count > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Primary.copy(alpha = 0.1f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text       = "$count vendors",
                            fontFamily = Roboto,
                            fontSize   = 12.sp,
                            color      = Primary
                        )
                    }
                }
            }
        }
    }

    when (vendorsState) {

        is AuthResult.Loading -> {
            item(key = "loading") {
                Box(
                    modifier         = Modifier.fillMaxWidth().padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary, modifier = Modifier.size(32.dp))
                }
            }
        }

        is AuthResult.Error -> {
            item(key = "error") {
                EmptyState(
                    icon     = Icons.Default.Store,
                    title    = "Vendors nahi mile",
                    subtitle = vendorsState.message
                )
            }
        }

        is AuthResult.Success<*> -> {
            @Suppress("UNCHECKED_CAST")
            val vendors = vendorsState.data as? List<Vendor> ?: emptyList()

            if (vendors.isEmpty()) {
                item(key = "empty") {
                    EmptyState(
                        icon     = Icons.Default.Store,
                        title    = "No Vendors Available",
                        subtitle = "Is part ke liye koi vendor nahi hai"
                    )
                }
            } else {
                if (vendors.size == 1) {
                    item(key = "single_vendor_banner") {
                        SingleVendorBanner(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                        )
                    }
                }

                items(items = vendors, key = { it.id }) { vendor ->
                    VendorCard(
                        vendor   = vendor,
                        onBuyNow = { /* TODO: Payment */ },
                        onChat   = {
                            // ✅ navController ki zaroorat nahi — lambda call karo
                            onChatClick(vendor.id, vendor.name)
                        },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }
            }
        }

        null -> {
            item(key = "null_state") {
                Box(
                    modifier         = Modifier.fillMaxWidth().padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary, modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

// ── Header ────────────────────────────────────────────
@Composable
private fun PartDetailHeader(
    part   : Part,
    onBack : () -> Unit
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(DashCardBg),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector        = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint               = DashTextPrimary,
                    modifier           = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text       = part.name,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp,
                color      = DashTextPrimary
            )
            Text(
                text       = part.category,
                fontFamily = Roboto,
                fontSize   = 12.sp,
                color      = DashTextSecondary
            )
        }
    }
}

// ── Part Info Card ────────────────────────────────────
@Composable
private fun PartInfoCard(
    part     : Part,
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(alpha = 0.04f))
            .clip(RoundedCornerShape(16.dp))
            .background(DashCardBg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
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
                        modifier           = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text       = part.name,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp,
                        color      = DashTextPrimary
                    )
                    Text(
                        text       = "Compatible: ${part.carModel}",
                        fontFamily = Roboto,
                        fontSize   = 12.sp,
                        color      = DashTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color.Black.copy(alpha = 0.05f))
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatItem(label = "Price From", value = "Rs ${part.price.toInt()}")
                StatItem(label = "Rating",     value = "⭐ ${part.rating}")
                StatItem(label = "Vendors",    value = "${part.vendorCount}")
            }

            if (part.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color.Black.copy(alpha = 0.05f))
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text       = "Description",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 13.sp,
                    color      = DashTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text       = part.description,
                    fontFamily = Roboto,
                    fontSize   = 13.sp,
                    color      = DashTextSecondary,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

// ── Stat Item ─────────────────────────────────────────
@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text       = value,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize   = 15.sp,
            color      = DashTextPrimary
        )
        Text(
            text       = label,
            fontFamily = Roboto,
            fontSize   = 11.sp,
            color      = DashTextLight
        )
    }
}

// ── Single Vendor Banner ──────────────────────────────
@Composable
private fun SingleVendorBanner(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Primary.copy(alpha = 0.08f))
            .padding(12.dp)
    ) {
        Text(
            text       = "ℹ️ Only one vendor available for this part",
            fontFamily = Roboto,
            fontSize   = 13.sp,
            color      = Primary
        )
    }
}