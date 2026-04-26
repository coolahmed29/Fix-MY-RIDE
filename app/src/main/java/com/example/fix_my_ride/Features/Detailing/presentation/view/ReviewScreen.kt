package com.example.fix_my_ride.Features.Detailing.presentation.view


// Features/Detailing/Presentation/View/ReviewScreen.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fix_my_ride.Features.Detailing.presentation.viewmodel.ReviewViewModel
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import com.example.fix_my_ride.ui.Components.AppButton
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
fun ReviewScreen(
    bookingId  : String,
    packageId  : String,
    packageName: String,
    onBack     : () -> Unit,
    onSubmitted: () -> Unit,
    viewModel  : ReviewViewModel = hiltViewModel()
) {
    val rating      by viewModel.rating.collectAsStateWithLifecycle()
    val reviewText  by viewModel.reviewText.collectAsStateWithLifecycle()
    val reviewState by viewModel.reviewState.collectAsStateWithLifecycle()

    // Success — navigate back
    LaunchedEffect(reviewState) {
        if (reviewState is AuthResult.Success) {
            viewModel.resetReviewState()
            onSubmitted()
        }
    }

    val isLoading    = reviewState is AuthResult.Loading
    val errorMessage = (reviewState as? AuthResult.Error)?.message

    Scaffold(
        containerColor = DashBackground,
        bottomBar      = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DashBackground)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                AppButton(
                    text      = "Submit Review",
                    onClick   = {
                        viewModel.submitReview(bookingId, packageId)
                    },
                    isLoading = isLoading,
                    enabled   = rating > 0
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {

            // ── Header ─────────────────────────────────
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
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

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text       = "Rate & Review",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp,
                        color      = DashTextPrimary
                    )
                    Text(
                        text       = packageName,
                        fontFamily = Roboto,
                        fontSize   = 12.sp,
                        color      = DashTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Star Rating ────────────────────────────
            Text(
                text       = "Your Rating",
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 16.sp,
                color      = DashTextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (1..5).forEach { star ->
                    Icon(
                        imageVector = if (star <= rating)
                            Icons.Default.Star
                        else
                            Icons.Outlined.StarOutline,
                        contentDescription = "Star $star",
                        tint               = IconYellow,
                        modifier           = Modifier
                            .size(40.dp)
                            .clickable { viewModel.onRatingChange(star) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (rating) {
                    1    -> "Poor"
                    2    -> "Fair"
                    3    -> "Good"
                    4    -> "Very Good"
                    5    -> "Excellent!"
                    else -> "Tap a star to rate"
                },
                fontFamily = Roboto,
                fontSize   = 13.sp,
                color      = if (rating > 0) IconYellow else DashTextLight
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Review Text ────────────────────────────
            Text(
                text       = "Write a Review (Optional)",
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 16.sp,
                color      = DashTextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value         = reviewText,
                onValueChange = viewModel::onReviewTextChange,
                placeholder   = {
                    Text(
                        text       = "Share your experience...",
                        fontFamily = Roboto,
                        color      = DashTextLight
                    )
                },
                modifier      = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                maxLines      = 6,
                shape         = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction      = ImeAction.Default
                ),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = Primary,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.2f),
                    focusedTextColor     = DashTextPrimary,
                    unfocusedTextColor   = DashTextPrimary,
                    cursorColor          = Primary
                )
            )

            // ── Error ──────────────────────────────────
            errorMessage?.let { msg ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text       = msg,
                    fontFamily = Roboto,
                    fontSize   = 13.sp,
                    color      = Color.Red
                )
            }
        }
    }
}