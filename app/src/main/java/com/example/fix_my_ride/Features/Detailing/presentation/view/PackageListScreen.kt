package com.example.fix_my_ride.Features.Detailing.presentation.view

// Features/Detailing/Presentation/View/PackageDetailScreen.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fix_my_ride.Features.Detailing.domain.model.DetailingPackage
import com.example.fix_my_ride.Features.Detailing.domain.model.PackageType
import com.example.fix_my_ride.Features.Detailing.presentation.viewmodel.PackageListViewModel
import com.example.fix_my_ride.Features.Detailing.presentation.viewmodel.PackageSortOption
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.ui.Components.CategoryChip
import com.example.fix_my_ride.ui.Components.EmptyState
import com.example.fix_my_ride.ui.theme.DashBackground
import com.example.fix_my_ride.ui.theme.DashCardBg
import com.example.fix_my_ride.ui.theme.DashTextLight
import com.example.fix_my_ride.ui.theme.DashTextPrimary
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.IconYellow
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
fun PackageListScreen(
    onPackageClick : (DetailingPackage) -> Unit,
    onBack         : () -> Unit,
    viewModel      : PackageListViewModel = hiltViewModel()
) {
    val packagesState by viewModel.packagesState
        .collectAsStateWithLifecycle()
    val selectedType  by viewModel.selectedType
        .collectAsStateWithLifecycle()
    val sortOption    by viewModel.sortOption
        .collectAsStateWithLifecycle()

    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(containerColor = DashBackground) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ── Header ─────────────────────────────────
            PackageListHeader(
                onBack       = onBack,
                sortOption   = sortOption,
                showSortMenu = showSortMenu,
                onSortToggle = { showSortMenu = !showSortMenu },
                onSortSelect = {
                    viewModel.onSortSelect(it)
                    showSortMenu = false
                }
            )

            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {

                // ── Type Filter Chips ──────────────────
                item(key = "filters") {
                    LazyRow(
                        contentPadding        = PaddingValues(
                            horizontal = 20.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier              = Modifier.padding(vertical = 12.dp)
                    ) {
                        // All chip
                        item {
                            CategoryChip(
                                label      = "All",
                                isSelected = selectedType == null,
                                onClick    = { viewModel.onTypeSelect(null) }
                            )
                        }

                        // Package type chips
                        items(PackageType.values().toList()) { type ->
                            CategoryChip(
                                label      = type.displayName,
                                isSelected = selectedType == type,
                                onClick    = { viewModel.onTypeSelect(type) }
                            )
                        }
                    }
                }

                // ── Packages ───────────────────────────
                when (val state = packagesState) {

                    is AuthResult.Loading -> {
                        item(key = "loading") {
                            Box(
                                modifier         = Modifier
                                    .fillMaxWidth()
                                    .padding(60.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color    = Primary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }

                    is AuthResult.Error -> {
                        item(key = "error") {
                            EmptyState(
                                icon     = Icons.Default.AutoAwesome,
                                title    = "Packages load nahi hue",
                                subtitle = state.message
                            )
                        }
                    }

                    is AuthResult.Success -> {
                        if (state.data.isEmpty()) {
                            item(key = "empty") {
                                EmptyState(
                                    icon     = Icons.Default.AutoAwesome,
                                    title    = "No Packages Available",
                                    subtitle = "Abhi koi package available nahi hai"
                                )
                            }
                        } else {
                            items(
                                items = state.data,
                                key   = { it.id }
                            ) { pkg ->
                                PackageCard(
                                    pkg      = pkg,
                                    onClick  = {
                                        viewModel.onPackageSelect(pkg)
                                        onPackageClick(pkg)
                                    },
                                    modifier = Modifier.padding(
                                        horizontal = 20.dp,
                                        vertical   = 6.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Header ────────────────────────────────────────────
@Composable
private fun PackageListHeader(
    onBack       : () -> Unit,
    sortOption   : PackageSortOption,
    showSortMenu : Boolean,
    onSortToggle : () -> Unit,
    onSortSelect : (PackageSortOption) -> Unit
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Back Button
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

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text       = "Detailing Packages",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize   = 20.sp,
                color      = DashTextPrimary
            )
        }

        // Sort Button + Menu
        Box {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(DashCardBg),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onSortToggle) {
                    Icon(
                        imageVector        = Icons.Default.FilterList,
                        contentDescription = "Sort",
                        tint               = Primary,
                        modifier           = Modifier.size(20.dp)
                    )
                }
            }

            DropdownMenu(
                expanded        = showSortMenu,
                onDismissRequest = { onSortToggle() }
            ) {
                PackageSortOption.values().forEach { option ->
                    DropdownMenuItem(
                        text    = {
                            Text(
                                text       = option.label,
                                fontFamily = Roboto,
                                color      = if (sortOption == option)
                                    Primary else DashTextPrimary
                            )
                        },
                        onClick = { onSortSelect(option) }
                    )
                }
            }
        }
    }
}

// ── Package Card ──────────────────────────────────────
@Composable
private fun PackageCard(
    pkg      : DetailingPackage,
    onClick  : () -> Unit,
    modifier : Modifier = Modifier
) {
    Card(
        modifier  = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 4.dp,
                shape        = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f)
            )
            .clickable { onClick() },
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = DashCardBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ── Type Badge + Rating ────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Type Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Primary.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text       = pkg.type.displayName,
                        fontFamily = Roboto,
                        fontSize   = 11.sp,
                        color      = Primary,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Rating
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
                        text       = pkg.rating.toString(),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium,
                        fontSize   = 12.sp,
                        color      = DashTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── Name ──────────────────────────────────
            Text(
                text       = pkg.name,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize   = 16.sp,
                color      = DashTextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            // ── Description ───────────────────────────
            Text(
                text       = pkg.description,
                fontFamily = Roboto,
                fontSize   = 13.sp,
                color      = DashTextSecondary,
                maxLines   = 2,
                overflow   = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ── Price + Duration ──────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text       = "Rs ${pkg.price.toInt()}",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp,
                    color      = Primary
                )

                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector        = Icons.Default.Timer,
                        contentDescription = null,
                        tint               = DashTextLight,
                        modifier           = Modifier.size(14.dp)
                    )
                    Text(
                        text       = "${pkg.durationHours}h",
                        fontFamily = Roboto,
                        fontSize   = 12.sp,
                        color      = DashTextLight
                    )
                }
            }
        }
    }
}