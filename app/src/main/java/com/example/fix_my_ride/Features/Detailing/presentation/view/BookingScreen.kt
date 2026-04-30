package com.example.fix_my_ride.Features.Detailing.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.fix_my_ride.Features.Detailing.domain.model.DetailingPackage
import com.example.fix_my_ride.Features.Detailing.domain.model.TimeSlot
import com.example.fix_my_ride.Features.Detailing.presentation.viewmodel.BookingViewModel
import com.example.fix_my_ride.ui.Components.AppButton
import com.example.fix_my_ride.ui.theme.DashBackground
import com.example.fix_my_ride.ui.theme.DashCardBg
import com.example.fix_my_ride.ui.theme.DashTextPrimary
import com.example.fix_my_ride.ui.theme.DashTextSecondary
import com.example.fix_my_ride.ui.theme.IconGreen
import com.example.fix_my_ride.ui.theme.Montserrat
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.Roboto
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BookingScreen(
    pkg           : DetailingPackage,
    onBack        : () -> Unit,
    onBookingDone : () -> Unit,
    viewModel     : BookingViewModel = hiltViewModel()
) {
    val slotsState   by viewModel.slotsState.collectAsStateWithLifecycle()
    val selectedSlot by viewModel.selectedSlot.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val bookingState by viewModel.bookingState.collectAsStateWithLifecycle()

    val dates = remember {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        (0..6).map { days ->
            LocalDate.now().plusDays(days.toLong()).format(formatter)
        }
    }

    Scaffold(
        containerColor = DashBackground,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DashBackground)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                AppButton(
                    text      = "Confirm Booking",
                    onClick   = {
                        android.util.Log.d("BOOKING", "Confirm button clicked")
                        android.util.Log.d("BOOKING", "selectedSlot: ${selectedSlot?.time}")
                        android.util.Log.d("BOOKING", "selectedDate: $selectedDate")
                        viewModel.createBooking(
                            packageId    = pkg.id,
                            packageName  = pkg.name,
                            price        = pkg.price,
                            workshopName = pkg.workshopName
                        )
                    },
                    isLoading = bookingState is AuthResult.Loading,
                    enabled   = selectedSlot != null
                )
            }
        }
    ) { padding ->

        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            item(key = "header") {
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

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text       = "Select Date & Time",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 20.sp,
                            color      = DashTextPrimary
                        )
                        Text(
                            text       = pkg.name,
                            fontFamily = Roboto,
                            fontSize   = 12.sp,
                            color      = DashTextSecondary
                        )
                    }
                }
            }

            item(key = "date_title") {
                Text(
                    text       = "Select Date",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 16.sp,
                    color      = DashTextPrimary,
                    modifier   = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            item(key = "dates") {
                LazyRow(
                    contentPadding        = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(dates) { date ->
                        DateChip(
                            date       = date,
                            isSelected = selectedDate == date,
                            onClick    = {
                                android.util.Log.d("BOOKING", "Date selected: $date")
                                viewModel.loadSlots(pkg.id, date)
                            }
                        )
                    }
                }
            }

            item(key = "slots_title") {
                Text(
                    text       = "Select Time",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 16.sp,
                    color      = DashTextPrimary,
                    modifier   = Modifier.padding(
                        start  = 20.dp,
                        end    = 20.dp,
                        top    = 20.dp,
                        bottom = 8.dp
                    )
                )
            }

            item(key = "legend") {
                Row(
                    modifier              = Modifier.padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LegendItem(color = IconGreen,                  label = "Available")
                    LegendItem(color = Color.Gray.copy(alpha = 0.4f), label = "Booked")
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            when (val state = slotsState) {
                is AuthResult.Loading -> {
                    item(key = "slots_loading") {
                        Box(
                            modifier         = Modifier.fillMaxWidth().padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Primary, modifier = Modifier.size(28.dp))
                        }
                    }
                }

                is AuthResult.Success -> {
                    item(key = "slots_grid") {
                        TimeSlotsGrid(
                            slots        = state.data,
                            selectedSlot = selectedSlot,
                            onSlotClick  = { slot ->
                                android.util.Log.d("BOOKING", "Slot tapped: ${slot.time}, available: ${slot.isAvailable}")
                                viewModel.onSlotSelect(slot)
                            },
                            modifier     = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }

                is AuthResult.Error -> {
                    item(key = "slots_error") {
                        Text(
                            text     = state.message,
                            color    = Color.Red,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }

                else -> {}
            }

            if (bookingState is AuthResult.Error) {
                item(key = "booking_error") {
                    Text(
                        text       = (bookingState as AuthResult.Error).message,
                        color      = Color.Red,
                        fontFamily = Roboto,
                        fontSize   = 13.sp,
                        modifier   = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}

// ── Date Chip ─────────────────────────────────────────
@Composable
private fun DateChip(
    date       : String,
    isSelected : Boolean,
    onClick    : () -> Unit
) {
    val displayDate = remember(date) {
        val parts = date.split("-")
        "${parts[2]}\n${
            when (parts[1]) {
                "01" -> "Jan"; "02" -> "Feb"; "03" -> "Mar"
                "04" -> "Apr"; "05" -> "May"; "06" -> "Jun"
                "07" -> "Jul"; "08" -> "Aug"; "09" -> "Sep"
                "10" -> "Oct"; "11" -> "Nov"; else -> "Dec"
            }
        }"
    }

    Box(
        modifier = Modifier
            .size(width = 56.dp, height = 64.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) Primary else DashCardBg)
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = Color.Gray.copy(alpha = 0.1f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = displayDate,
            fontFamily = Montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 13.sp,
            color      = if (isSelected) Color.White else DashTextPrimary,
            lineHeight = 18.sp,
            textAlign  = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

// ── Time Slots Grid ───────────────────────────────────
@Composable
private fun TimeSlotsGrid(
    slots        : List<TimeSlot>,
    selectedSlot : TimeSlot?,
    onSlotClick  : (TimeSlot) -> Unit,
    modifier     : Modifier = Modifier
) {
    Column(
        modifier            = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        slots.chunked(3).forEach { rowSlots ->
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowSlots.forEach { slot ->
                    SlotChip(
                        slot       = slot,
                        isSelected = selectedSlot?.id == slot.id,
                        onClick    = { onSlotClick(slot) },
                        modifier   = Modifier.weight(1f)
                    )
                }
                repeat(3 - rowSlots.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// ── Slot Chip ─────────────────────────────────────────
@Composable
private fun SlotChip(
    slot       : TimeSlot,
    isSelected : Boolean,
    onClick    : () -> Unit,
    modifier   : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                when {
                    isSelected       -> Primary
                    slot.isAvailable -> IconGreen.copy(alpha = 0.1f)
                    else             -> Color.Gray.copy(alpha = 0.1f)
                }
            )
            .clickable(enabled = slot.isAvailable) {  // ✅ fix
                android.util.Log.d("BOOKING", "SlotChip clicked: ${slot.time}")
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = slot.time,
            fontFamily = Roboto,
            fontSize   = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color      = when {
                isSelected       -> Color.White
                slot.isAvailable -> IconGreen
                else             -> Color.Gray
            }
        )
    }
}

// ── Legend Item ───────────────────────────────────────
@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text       = label,
            fontFamily = Roboto,
            fontSize   = 12.sp,
            color      = DashTextSecondary
        )
    }
}