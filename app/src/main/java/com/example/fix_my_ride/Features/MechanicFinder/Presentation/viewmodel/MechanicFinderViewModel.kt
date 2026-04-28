package com.example.fix_my_ride.Features.MechanicFinder.Presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceRequest
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.RequestStatus
import com.example.fix_my_ride.Features.MechanicFinder.domain.usecase.GetNearbyMechanicsUseCase
import com.example.fix_my_ride.Features.MechanicFinder.domain.usecase.SendServiceRequestUseCase
import com.example.fix_my_ride.Features.MechanicFinder.domain.usecase.GetMechanicProfileUseCase
import com.example.fix_my_ride.Features.MechanicFinder.domain.usecase.UpdateAvailabilityUseCase
import com.example.fix_my_ride.Features.MechanicFinder.domain.usecase.TrackMechanicLocationUseCase
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MechanicFinderViewModel @Inject constructor(
    private val getNearbyMechanicsUseCase: GetNearbyMechanicsUseCase,
    private val sendServiceRequestUseCase: SendServiceRequestUseCase,
    private val getMechanicProfileUseCase: GetMechanicProfileUseCase,
    private val updateAvailabilityUseCase: UpdateAvailabilityUseCase,
    private val trackMechanicLocationUseCase: TrackMechanicLocationUseCase
) : ViewModel() {

    // Nearby mechanics state
    private val _nearbyMechanicsState = MutableStateFlow<AuthResult<*>?>(null)
    val nearbyMechanicsState: StateFlow<AuthResult<*>?> = _nearbyMechanicsState.asStateFlow()

    // Selected mechanic state
    private val _selectedMechanic = MutableStateFlow<Mechanic?>(null)
    val selectedMechanic: StateFlow<Mechanic?> = _selectedMechanic.asStateFlow()

    // Mechanic profile state
    private val _mechanicProfileState = MutableStateFlow<AuthResult<*>?>(null)
    val mechanicProfileState: StateFlow<AuthResult<*>?> = _mechanicProfileState.asStateFlow()

    // Service request state
    private val _serviceRequestState = MutableStateFlow<AuthResult<*>?>(null)
    val serviceRequestState: StateFlow<AuthResult<*>?> = _serviceRequestState.asStateFlow()

    // Mechanic location tracking state
    private val _mechanicLocationState = MutableStateFlow<AuthResult<*>?>(null)
    val mechanicLocationState: StateFlow<AuthResult<*>?> = _mechanicLocationState.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error state
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // ── Fetch Nearby Mechanics ─────────────────────
    fun fetchNearbyMechanics(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _nearbyMechanicsState.value = AuthResult.Loading // ✅ یہ line check کریں

            val result = getNearbyMechanicsUseCase(latitude, longitude, radiusKm)
            _nearbyMechanicsState.value = result

            if (result is AuthResult.Error) {
                _errorMessage.value = result.message
            }
            _isLoading.value = false
        }
    }

    // ── Get Mechanic Profile ──────────────────────
    fun getMechanicProfile(mechanicId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _mechanicProfileState.value = AuthResult.Loading // ✅ یہ line بھی

            val result = getMechanicProfileUseCase(mechanicId)
            _mechanicProfileState.value = result

            if (result is AuthResult.Success<*>) {
                _selectedMechanic.value = result.data as? Mechanic
            } else if (result is AuthResult.Error) {
                _errorMessage.value = result.message
            }
            _isLoading.value = false
        }
    }

    // ── Send Service Request ──────────────────────
    fun sendServiceRequest(serviceRequest: ServiceRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _serviceRequestState.value = AuthResult.Loading // ✅ یہ line بھی

            val result = sendServiceRequestUseCase(serviceRequest)
            _serviceRequestState.value = result

            if (result is AuthResult.Error) {
                _errorMessage.value = result.message
            }
            _isLoading.value = false
        }
    }

    // ── Track Mechanic Location ────────────────────
    fun trackMechanicLocation(mechanicId: String) {
        viewModelScope.launch {
            trackMechanicLocationUseCase(mechanicId).collect { result ->
                _mechanicLocationState.value = result
            }
        }
    }

    // ── Update Mechanic Availability ──────────────
    fun updateMechanicAvailability(mechanicId: String, isAvailable: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = updateAvailabilityUseCase(mechanicId, isAvailable)

            if (result is AuthResult.Error) {
                _errorMessage.value = result.message
            }
            _isLoading.value = false
        }
    }

    // ── Select Mechanic ────────────────────────────
    fun onMechanicSelect(mechanic: Mechanic) {
        _selectedMechanic.value = mechanic
    }

    // ── Clear Error Message ────────────────────────
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // ── Clear Selected Mechanic ────────────────────
    fun clearSelectedMechanic() {
        _selectedMechanic.value = null
    }

    // ── Reset Service Request State ────────────────
    fun resetServiceRequestState() {
        _serviceRequestState.value = null
    }
}