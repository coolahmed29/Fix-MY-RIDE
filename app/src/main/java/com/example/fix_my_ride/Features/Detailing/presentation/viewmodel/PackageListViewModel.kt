package com.example.fix_my_ride.Features.Detailing.presentation.viewmodel

// Features/Detailing/Presentation/PackageListViewModel.kt


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fix_my_ride.Features.Detailing.domain.model.DetailingPackage
import com.example.fix_my_ride.Features.Detailing.domain.model.PackageType
import com.example.fix_my_ride.Features.Detailing.domain.usecase.GetPackagesUseCase
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PackageListViewModel @Inject constructor(
    private val getPackagesUseCase: GetPackagesUseCase
) : ViewModel() {

    // ── Packages State ────────────────────────────────
    private val _packagesState =
        MutableStateFlow<AuthResult<List<DetailingPackage>>>(AuthResult.Loading)
    val packagesState: StateFlow<AuthResult<List<DetailingPackage>>> =
        _packagesState.asStateFlow()

    // ── Selected Filter ───────────────────────────────
    private val _selectedType = MutableStateFlow<PackageType?>(null)
    val selectedType: StateFlow<PackageType?> = _selectedType.asStateFlow()

    // ── Sort Option ───────────────────────────────────
    private val _sortOption = MutableStateFlow(PackageSortOption.DEFAULT)
    val sortOption: StateFlow<PackageSortOption> = _sortOption.asStateFlow()

    // ── Selected Package (detail ke liye) ─────────────
    private val _selectedPackage = MutableStateFlow<DetailingPackage?>(null)
    val selectedPackage: StateFlow<DetailingPackage?> =
        _selectedPackage.asStateFlow()

    init {
        loadPackages()
    }

    // ── Load Packages ─────────────────────────────────
    fun loadPackages() {
        viewModelScope.launch {
            _packagesState.value = AuthResult.Loading
            getPackagesUseCase().collect { result ->
                _packagesState.value = when (result) {
                    is AuthResult.Success -> {
                        AuthResult.Success(
                            applyFiltersAndSort(
                                packages   = result.data,
                                type       = _selectedType.value,
                                sortOption = _sortOption.value
                            )
                        )
                    }
                    else -> result
                }
            }
        }
    }

    // ── Filter by Type ────────────────────────────────
    fun onTypeSelect(type: PackageType?) {
        _selectedType.value = type
        val current = _packagesState.value
        if (current is AuthResult.Success) {
            _packagesState.value = AuthResult.Success(
                applyFiltersAndSort(
                    packages   = current.data,
                    type       = type,
                    sortOption = _sortOption.value
                )
            )
        }
    }

    // ── Sort ──────────────────────────────────────────
    fun onSortSelect(option: PackageSortOption) {
        _sortOption.value = option
        val current = _packagesState.value
        if (current is AuthResult.Success) {
            _packagesState.value = AuthResult.Success(
                applyFiltersAndSort(
                    packages   = current.data,
                    type       = _selectedType.value,
                    sortOption = option
                )
            )
        }
    }

    // ── Select Package ────────────────────────────────
    fun onPackageSelect(pkg: DetailingPackage) {
        _selectedPackage.value = pkg
    }

    // ── Apply Filters + Sort ──────────────────────────
    private fun applyFiltersAndSort(
        packages   : List<DetailingPackage>,
        type       : PackageType?,
        sortOption : PackageSortOption
    ): List<DetailingPackage> {
        var result = packages

        // Type filter
        if (type != null) {
            result = result.filter { it.type == type }
        }

        // Sort
        result = when (sortOption) {
            PackageSortOption.DEFAULT  -> result
            PackageSortOption.PRICE_LOW  -> result.sortedBy { it.price }
            PackageSortOption.PRICE_HIGH -> result.sortedByDescending { it.price }
            PackageSortOption.POPULAR    -> result.sortedByDescending { it.rating }
        }

        return result
    }
}

enum class PackageSortOption(val label: String) {
    DEFAULT    ("Default"),
    PRICE_LOW  ("Low to High"),
    PRICE_HIGH ("High to Low"),
    POPULAR    ("Popular")
}