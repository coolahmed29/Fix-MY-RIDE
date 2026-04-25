package com.example.fix_my_ride.Features.SpareParts.Presentation.view

// Features/SpareParts/Presentation/View/SparePartsScreen.kt

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Part
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.Features.SpareParts.Presentation.viewmodel.SparePartsViewModel
import com.example.fix_my_ride.ui.Components.CategoryChip
import com.example.fix_my_ride.ui.Components.EmptyState
import com.example.fix_my_ride.ui.Components.PartCard
import com.example.fix_my_ride.ui.theme.DashBackground
import com.example.fix_my_ride.ui.theme.DashCardBg
import com.example.fix_my_ride.ui.theme.DashTextLight
import com.example.fix_my_ride.ui.theme.DashTextPrimary
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto

@Composable
fun SparePartsScreen(
    onPartClick : (Part) -> Unit,
    onBack      : () -> Unit,
    viewModel   : SparePartsViewModel = hiltViewModel()
) {
    val searchQuery      by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val partsState       by viewModel.partsState.collectAsStateWithLifecycle()

    // Static categories — Firestore se bhi aa sakti hain
    val categories = listOf(
        "All", "Engine", "Battery",
        "Tires", "Brakes", "Lights", "Filters"
    )

    Scaffold(
        containerColor = DashBackground
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // ── Header ─────────────────────────────────
            SparePartsHeader(onBack = onBack)

            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {

                // ── Search Bar ─────────────────────────
                item {
                    SparePartsSearchBar(
                        query    = searchQuery,
                        onQuery  = viewModel::onSearchQueryChange,
                        modifier = Modifier.padding(
                            horizontal = 20.dp,
                            vertical   = 16.dp
                        )
                    )
                }

                // ── Categories ─────────────────────────
                item {
                    LazyRow(
                        contentPadding        = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            CategoryChip(
                                label      = category,
                                isSelected = selectedCategory == category,
                                onClick    = {
                                    viewModel.onCategorySelect(category)
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // ── Parts Count ────────────────────────
                item {
                    if (partsState is AuthResult.Success) {
                        val count =
                            (partsState as AuthResult.Success).data.size
                        Text(
                            text       = "$count parts found",
                            fontFamily = Roboto,
                            fontSize   = 13.sp,
                            color      = DashTextSecondary,
                            modifier   = Modifier.padding(
                                horizontal = 20.dp,
                                vertical   = 4.dp
                            )
                        )
                    }
                }

                // ── Parts List ─────────────────────────
                when (val state = partsState) {

                    is AuthResult.Loading -> {
                        item {
                            Box(
                                modifier         = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 60.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Primary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                    }

                    is AuthResult.Error -> {
                        item {
                            EmptyState(
                                icon     = Icons.Default.Build,
                                title    = "Kuch ghalat hua",
                                subtitle = state.message,
                                modifier = Modifier.padding(top = 40.dp)
                            )
                        }
                    }

                    is AuthResult.Success -> {
                        if (state.data.isEmpty()) {
                            item {
                                EmptyState(
                                    icon     = Icons.Default.Search,
                                    title    = "No Parts Found",
                                    subtitle = "Try different keyword or category",
                                    modifier = Modifier.padding(top = 40.dp)
                                )
                            }
                        } else {
                            items(
                                items = state.data,
                                key   = { it.id }
                            ) { part ->
                                PartCard(
                                    part     = part,
                                    onClick  = { onPartClick(part) },
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
private fun SparePartsHeader(onBack: () -> Unit) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical   = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
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

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text       = "Spare Parts",
            fontFamily = Montserrat,
            fontWeight = FontWeight.Bold,
            fontSize   = 20.sp,
            color      = DashTextPrimary
        )
    }
}

// ── Search Bar ────────────────────────────────────────
@Composable
private fun SparePartsSearchBar(
    query    : String,
    onQuery  : (String) -> Unit,
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 4.dp,
                shape        = RoundedCornerShape(30.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f)
            )
            .clip(RoundedCornerShape(30.dp))
            .background(DashCardBg)
    ) {
        TextField(
            value         = query,
            onValueChange = onQuery,
            placeholder   = {
                Text(
                    text       = "Search parts, model...",
                    fontFamily = Roboto,
                    color      = DashTextLight,
                    fontSize   = 14.sp
                )
            },
            leadingIcon   = {
                Icon(
                    imageVector        = Icons.Default.Search,
                    contentDescription = null,
                    tint               = DashTextLight,
                    modifier           = Modifier.size(20.dp)
                )
            },
            singleLine    = true,
            modifier      = Modifier.fillMaxWidth(),
            colors        = TextFieldDefaults.colors(
                focusedContainerColor   = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor   = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor        = DashTextPrimary,
                unfocusedTextColor      = DashTextPrimary,
                cursorColor             = Primary
            )
        )
    }
}