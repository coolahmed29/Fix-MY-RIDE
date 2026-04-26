package com.example.fix_my_ride.Features.Detailing.presentation.viewmodel

// Features/Detailing/Presentation/ReviewViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fix_my_ride.Features.Detailing.domain.model.Booking
import com.example.fix_my_ride.Features.Detailing.domain.model.BookingStatus
import com.example.fix_my_ride.Features.Detailing.domain.usecase.GetBookingsUseCase
import com.example.fix_my_ride.Features.Detailing.domain.usecase.SubmitReviewUseCase
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val submitReviewUseCase : SubmitReviewUseCase,
    private val getBookingsUseCase  : GetBookingsUseCase
) : ViewModel() {

    // ── Completed Bookings ────────────────────────────
    private val _bookingsState =
        MutableStateFlow<AuthResult<List<Booking>>>(AuthResult.Loading)
    val bookingsState: StateFlow<AuthResult<List<Booking>>> =
        _bookingsState.asStateFlow()

    // ── Review Submit State ───────────────────────────
    private val _reviewState =
        MutableStateFlow<AuthResult<Unit>?>(null)
    val reviewState: StateFlow<AuthResult<Unit>?> =
        _reviewState.asStateFlow()

    // ── Rating ────────────────────────────────────────
    private val _rating = MutableStateFlow(0)
    val rating: StateFlow<Int> = _rating.asStateFlow()

    // ── Review Text ───────────────────────────────────
    private val _reviewText = MutableStateFlow("")
    val reviewText: StateFlow<String> = _reviewText.asStateFlow()

    init {
        loadCompletedBookings()
    }

    // ── Load Completed Bookings ───────────────────────
    private fun loadCompletedBookings() {
        viewModelScope.launch {
            _bookingsState.value = AuthResult.Loading
            getBookingsUseCase(BookingStatus.COMPLETED)
                .collect { _bookingsState.value = it }
        }
    }

    fun onRatingChange(rating: Int)      { _rating.value = rating }
    fun onReviewTextChange(text: String) { _reviewText.value = text }

    // ── Submit Review ─────────────────────────────────
    fun submitReview(bookingId: String, packageId: String) {

        // Sirf completed booking pe review
        val bookings = (_bookingsState.value as? AuthResult.Success)?.data
        val booking  = bookings?.find { it.id == bookingId }

        if (booking?.status != BookingStatus.COMPLETED) {
            _reviewState.value = AuthResult.Error(
                "You cannot review this service yet."
            )
            return
        }

        if (_rating.value == 0) {
            _reviewState.value = AuthResult.Error("Rating zaroori hai")
            return
        }

        viewModelScope.launch {
            _reviewState.value = AuthResult.Loading
            _reviewState.value = submitReviewUseCase(
                bookingId  = bookingId,
                packageId  = packageId,
                rating     = _rating.value,
                reviewText = _reviewText.value
            )
        }
    }

    fun resetReviewState() { _reviewState.value = null }
}