package com.example.fix_my_ride.Features.MechanicFinder.Presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.Mechanic
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.MechanicReview
import com.example.fix_my_ride.Features.MechanicFinder.domain.model.ServiceRequest
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
import android.util.Log

@HiltViewModel
class MechanicFinderViewModel @Inject constructor(
    private val getNearbyMechanicsUseCase: GetNearbyMechanicsUseCase,
    private val sendServiceRequestUseCase: SendServiceRequestUseCase,
    private val getMechanicProfileUseCase: GetMechanicProfileUseCase,
    private val updateAvailabilityUseCase: UpdateAvailabilityUseCase,
    private val trackMechanicLocationUseCase: TrackMechanicLocationUseCase
) : ViewModel() {

    // ── State Flows ────────────────────────────────
    private val _nearbyMechanicsState = MutableStateFlow<AuthResult<*>?>(null)
    val nearbyMechanicsState: StateFlow<AuthResult<*>?> = _nearbyMechanicsState.asStateFlow()

    private val _selectedMechanic = MutableStateFlow<Mechanic?>(null)
    val selectedMechanic: StateFlow<Mechanic?> = _selectedMechanic.asStateFlow()

    private val _mechanicProfileState = MutableStateFlow<AuthResult<*>?>(null)
    val mechanicProfileState: StateFlow<AuthResult<*>?> = _mechanicProfileState.asStateFlow()

    private val _serviceRequestState = MutableStateFlow<AuthResult<*>?>(null)
    val serviceRequestState: StateFlow<AuthResult<*>?> = _serviceRequestState.asStateFlow()

    private val _mechanicLocationState = MutableStateFlow<AuthResult<*>?>(null)
    val mechanicLocationState: StateFlow<AuthResult<*>?> = _mechanicLocationState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // ── Fetch Nearby Mechanics from Firebase ───────
    fun fetchNearbyMechanics(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 10.0
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _nearbyMechanicsState.value = AuthResult.Loading

            Log.d("MechanicViewModel", "Fetching mechanics at: $latitude, $longitude with radius: ${radiusKm}km")

            try {
                // ✅ Firebase سے actual data لیں
                val result = getNearbyMechanicsUseCase(latitude, longitude, radiusKm)

                when (result) {
                    is AuthResult.Success<*> -> {
                        val mechanics = result.data as? List<Mechanic> ?: emptyList()
                        Log.d("MechanicViewModel", "✅ Found ${mechanics.size} mechanics")

                        if (mechanics.isEmpty()) {
                            Log.w("MechanicViewModel", "No mechanics found, but use case succeeded")
                            _errorMessage.value = "No mechanics available in your area"
                        }

                        _nearbyMechanicsState.value = result
                    }

                    is AuthResult.Error -> {
                        Log.e("MechanicViewModel", "❌ Error: ${result.message}")
                        _errorMessage.value = result.message
                        _nearbyMechanicsState.value = result
                    }

                    is AuthResult.Loading -> {
                        // Already set above
                    }
                }
            } catch (e: Exception) {
                Log.e("MechanicViewModel", "❌ Exception: ${e.message}", e)
                val error = AuthResult.Error(e.message ?: "Unknown error")
                _nearbyMechanicsState.value = error
                _errorMessage.value = e.message
            }

            _isLoading.value = false
        }
    }

    // ── Get Mechanic Profile from Firebase ──────────
    fun getMechanicProfile(mechanicId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _mechanicProfileState.value = AuthResult.Loading

            Log.d("MechanicViewModel", "Fetching profile for mechanic: $mechanicId")

            try {
                // ✅ Firebase سے actual profile لیں
                val result = getMechanicProfileUseCase(mechanicId)

                when (result) {
                    is AuthResult.Success<*> -> {
                        val mechanic = result.data as? Mechanic
                        Log.d("MechanicViewModel", "✅ Profile loaded: ${mechanic?.name}")
                        _selectedMechanic.value = mechanic
                        _mechanicProfileState.value = result
                    }

                    is AuthResult.Error -> {
                        Log.e("MechanicViewModel", "❌ Error: ${result.message}")
                        _errorMessage.value = result.message
                        _mechanicProfileState.value = result
                    }

                    is AuthResult.Loading -> {
                        // Already set above
                    }
                }
            } catch (e: Exception) {
                Log.e("MechanicViewModel", "❌ Exception: ${e.message}", e)
                val error = AuthResult.Error(e.message ?: "Unknown error")
                _mechanicProfileState.value = error
                _errorMessage.value = e.message
            }

            _isLoading.value = false
        }
    }

    // ── Send Service Request ────────────────────────
    fun sendServiceRequest(serviceRequest: ServiceRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _serviceRequestState.value = AuthResult.Loading

            Log.d("MechanicViewModel", "Sending service request for mechanic: ${serviceRequest.mechanicId}")

            try {
                // ✅ Firebase میں request save کریں
                val result = sendServiceRequestUseCase(serviceRequest)

                when (result) {
                    is AuthResult.Success<*> -> {
                        val request = result.data as? ServiceRequest
                        Log.d("MechanicViewModel", "✅ Request sent: ${request?.id}")
                        _serviceRequestState.value = result
                    }

                    is AuthResult.Error -> {
                        Log.e("MechanicViewModel", "❌ Error: ${result.message}")
                        _errorMessage.value = result.message
                        _serviceRequestState.value = result
                    }

                    is AuthResult.Loading -> {
                        // Already set above
                    }
                }
            } catch (e: Exception) {
                Log.e("MechanicViewModel", "❌ Exception: ${e.message}", e)
                val error = AuthResult.Error(e.message ?: "Failed to send request")
                _serviceRequestState.value = error
                _errorMessage.value = e.message
            }

            _isLoading.value = false
        }
    }

    // ── Track Mechanic Location ─────────────────────
    fun trackMechanicLocation(mechanicId: String) {
        viewModelScope.launch {
            Log.d("MechanicViewModel", "Starting location tracking for: $mechanicId")

            try {
                trackMechanicLocationUseCase(mechanicId).collect { result ->
                    when (result) {
                        is AuthResult.Success<*> -> {
                            val location = result.data as? Pair<Double, Double>
                            Log.d("MechanicViewModel", "📍 Location updated: $location")
                            _mechanicLocationState.value = result
                        }

                        is AuthResult.Error -> {
                            Log.e("MechanicViewModel", "❌ Tracking error: ${result.message}")
                            _mechanicLocationState.value = result
                        }

                        is AuthResult.Loading -> {
                            Log.d("MechanicViewModel", "Tracking in progress...")
                            _mechanicLocationState.value = result
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("MechanicViewModel", "❌ Tracking exception: ${e.message}", e)
                _mechanicLocationState.value = AuthResult.Error(e.message ?: "Tracking failed")
            }
        }
    }

    // ── Update Mechanic Availability ────────────────
    fun updateMechanicAvailability(mechanicId: String, isAvailable: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true

            Log.d("MechanicViewModel", "Updating availability for $mechanicId: $isAvailable")

            try {
                val result = updateAvailabilityUseCase(mechanicId, isAvailable)

                when (result) {
                    is AuthResult.Success<*> -> {
                        Log.d("MechanicViewModel", "✅ Availability updated")
                    }

                    is AuthResult.Error -> {
                        Log.e("MechanicViewModel", "❌ Error: ${result.message}")
                        _errorMessage.value = result.message
                    }

                    is AuthResult.Loading -> {
                        // In progress
                    }
                }
            } catch (e: Exception) {
                Log.e("MechanicViewModel", "❌ Exception: ${e.message}")
                _errorMessage.value = e.message
            }

            _isLoading.value = false
        }
    }

    // ── Select Mechanic ────────────────────────────
    fun onMechanicSelect(mechanic: Mechanic) {
        Log.d("MechanicViewModel", "Mechanic selected: ${mechanic.name}")
        _selectedMechanic.value = mechanic
    }

    // ── Clear Error Message ─────────────────────────
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // ── Clear Selected Mechanic ─────────────────────
    fun clearSelectedMechanic() {
        _selectedMechanic.value = null
    }

    // ── Reset Service Request State ─────────────────
    fun resetServiceRequestState() {
        _serviceRequestState.value = null
    }

    // ── Reset Mechanic Profile State ────────────────
    fun resetMechanicProfileState() {
        _mechanicProfileState.value = null
    }

    // ── Reset All States ────────────────────────────
    fun resetAllStates() {
        _nearbyMechanicsState.value = null
        _selectedMechanic.value = null
        _mechanicProfileState.value = null
        _serviceRequestState.value = null
        _mechanicLocationState.value = null
        _errorMessage.value = null
    }
}