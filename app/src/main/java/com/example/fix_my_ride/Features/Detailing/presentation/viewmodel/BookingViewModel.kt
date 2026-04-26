package com.example.fix_my_ride.Features.Detailing.presentation.viewmodel

// Features/Detailing/Presentation/BookingViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fix_my_ride.Features.Detailing.domain.model.Booking
import com.example.fix_my_ride.Features.Detailing.domain.model.TimeSlot
import com.example.fix_my_ride.Features.Detailing.domain.usecase.CreateBookingUseCase
import com.example.fix_my_ride.Features.Detailing.domain.usecase.GetAvailableSlotsUseCase
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val getAvailableSlotsUseCase : GetAvailableSlotsUseCase,
    private val createBookingUseCase     : CreateBookingUseCase
) : ViewModel() {

    private val _slotsState =
        MutableStateFlow<AuthResult<List<TimeSlot>>>(AuthResult.Loading)
    val slotsState: StateFlow<AuthResult<List<TimeSlot>>> =
        _slotsState.asStateFlow()

    private val _selectedDate = MutableStateFlow("")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedSlot = MutableStateFlow<TimeSlot?>(null)
    val selectedSlot: StateFlow<TimeSlot?> = _selectedSlot.asStateFlow()

    private val _bookingState =
        MutableStateFlow<AuthResult<Booking>?>(null)
    val bookingState: StateFlow<AuthResult<Booking>?> =
        _bookingState.asStateFlow()

    fun loadSlots(packageId: String, date: String) {
        _selectedDate.value = date
        viewModelScope.launch {
            _slotsState.value = AuthResult.Loading
            _slotsState.value = getAvailableSlotsUseCase(packageId, date)
        }
    }

    fun onSlotSelect(slot: TimeSlot) {
        if (slot.isAvailable) {
            _selectedSlot.value = slot
        }
    }

    fun createBooking(
        packageId    : String,
        packageName  : String,
        price        : Double,
        workshopName : String,
        workshopId   : String = ""   // ✅ Add kiya
    ) {
        val slot = _selectedSlot.value ?: return
        val date = _selectedDate.value

        viewModelScope.launch {
            _bookingState.value = AuthResult.Loading
            _bookingState.value = createBookingUseCase(
                packageId    = packageId,
                packageName  = packageName,
                price        = price,
                date         = date,
                timeSlot     = slot.time,
                workshopName = workshopName,
                workshopId   = workshopId
            )
        }
    }

    fun resetBookingState() { _bookingState.value = null }
}