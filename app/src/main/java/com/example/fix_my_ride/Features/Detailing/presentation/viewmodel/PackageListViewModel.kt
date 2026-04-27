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

    // ✅ Original list — kabhi change nahi hogi
    private val _allPackages = MutableStateFlow<List<DetailingPackage>>(emptyList())

    // ── UI ko yeh dikhao — filtered + sorted
    private val _packagesState =
        MutableStateFlow<AuthResult<List<DetailingPackage>>>(AuthResult.Loading)
    val packagesState: StateFlow<AuthResult<List<DetailingPackage>>> =
        _packagesState.asStateFlow()

    // ── Filters ───────────────────────────────────────
    private val _selectedType = MutableStateFlow<PackageType?>(null)
    val selectedType: StateFlow<PackageType?> = _selectedType.asStateFlow()

    private val _sortOption = MutableStateFlow(PackageSortOption.DEFAULT)
    val sortOption: StateFlow<PackageSortOption> = _sortOption.asStateFlow()

    // ── Selected Package ──────────────────────────────
    private val _selectedPackage = MutableStateFlow<DetailingPackage?>(null)
    val selectedPackage: StateFlow<DetailingPackage?> =
        _selectedPackage.asStateFlow()

    init {
        loadPackages()
    }

    // ── Load — Original list save karo ────────────────
    private fun loadPackages() {
        viewModelScope.launch {
            _packagesState.value = AuthResult.Loading
            getPackagesUseCase().collect { result ->
                when (result) {
                    is AuthResult.Success -> {
                        // ✅ Original list save karo
                        _allPackages.value = result.data

                        // UI ko filtered result do
                        applyAndUpdate()
                    }
                    else -> _packagesState.value = result
                }
            }
        }
    }

    // ── Type Filter ───────────────────────────────────
    fun onTypeSelect(type: PackageType?) {
        _selectedType.value = type
        // ✅ _allPackages se filter karo — UI state se nahi
        applyAndUpdate()
    }

    // ── Sort ──────────────────────────────────────────
    fun onSortSelect(option: PackageSortOption) {
        _sortOption.value = option
        applyAndUpdate()
    }

    // ── Package Select ────────────────────────────────
    fun onPackageSelect(pkg: DetailingPackage) {
        _selectedPackage.value = pkg
    }

    // ✅ Yeh function har baar _allPackages se fresh filter karta hai
    private fun applyAndUpdate() {
        val filtered = applyFiltersAndSort(
            packages   = _allPackages.value,   // ← Hamesha original list
            type       = _selectedType.value,
            sortOption = _sortOption.value
        )
        _packagesState.value = AuthResult.Success(filtered)
    }

    // ── Filter + Sort Logic ───────────────────────────
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
            PackageSortOption.DEFAULT    -> result
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