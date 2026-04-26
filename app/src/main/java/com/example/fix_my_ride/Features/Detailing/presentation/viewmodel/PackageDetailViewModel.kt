package com.example.fix_my_ride.Features.Detailing.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fix_my_ride.Features.Detailing.domain.model.DetailingPackage
import com.example.fix_my_ride.Features.Detailing.domain.usecase.GetPackageDetailUseCase
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PackageDetailViewModel @Inject constructor(
    private val getPackageDetailUseCase: GetPackageDetailUseCase
) : ViewModel() {

    // ── Package Detail State ──────────────────────────
    private val _packageState =
        MutableStateFlow<AuthResult<DetailingPackage>>(AuthResult.Loading)
    val packageState: StateFlow<AuthResult<DetailingPackage>> =
        _packageState.asStateFlow()

    // ── Selected Package — Navigation ke liye ─────────
    private val _selectedPackage = MutableStateFlow<DetailingPackage?>(null)
    val selectedPackage: StateFlow<DetailingPackage?> =
        _selectedPackage.asStateFlow()

    // ── Load Detail ───────────────────────────────────
    fun loadPackageDetail(packageId: String) {
        viewModelScope.launch {
            _packageState.value = AuthResult.Loading
            _packageState.value = getPackageDetailUseCase(packageId)

            // ✅ Success mein selected package set karo
            val result = _packageState.value
            if (result is AuthResult.Success) {
                _selectedPackage.value = result.data
            }
        }
    }

    // ── Select Package directly ───────────────────────
    // PackageList se direct pass karne ke liye
    fun setSelectedPackage(pkg: DetailingPackage) {
        _selectedPackage.value = pkg
    }

    fun resetState() {
        _packageState.value    = AuthResult.Loading
        _selectedPackage.value = null
    }
}