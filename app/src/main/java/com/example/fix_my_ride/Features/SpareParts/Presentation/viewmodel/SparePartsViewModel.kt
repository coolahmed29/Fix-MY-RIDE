package com.example.fix_my_ride.Features.SpareParts.Presentation.viewmodel

// Features/SpareParts/Presentation/SparePartsViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Part
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Vendor
import com.example.fix_my_ride.Features.SpareParts.Domain.usecase.GetVendorsForPartUseCase
import com.example.fix_my_ride.Features.SpareParts.Domain.usecase.SearchPartsUseCase
import com.example.fix_my_ride.Features.Authentication.Domain.model.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SparePartsViewModel @Inject constructor(
    private val searchPartsUseCase      : SearchPartsUseCase,
    private val getVendorsForPartUseCase: GetVendorsForPartUseCase
) : ViewModel() {

    // ── Search Query ──────────────────────────────────
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // ── Selected Category ─────────────────────────────
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // ── Parts State ───────────────────────────────────
    private val _partsState =
        MutableStateFlow<AuthResult<List<Part>>>(AuthResult.Loading)
    val partsState: StateFlow<AuthResult<List<Part>>> =
        _partsState.asStateFlow()

    // ── Vendors State ─────────────────────────────────
    private val _vendorsState =
        MutableStateFlow<AuthResult<List<Vendor>>?>(null)
    val vendorsState: StateFlow<AuthResult<List<Vendor>>?> =
        _vendorsState.asStateFlow()

    // ── Selected Part ─────────────────────────────────
    private val _selectedPart = MutableStateFlow<Part?>(null)
    val selectedPart: StateFlow<Part?> = _selectedPart.asStateFlow()

    // ── Sort Option ───────────────────────────────────
    private val _sortOption = MutableStateFlow(SortOption.PRICE)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    init {
        loadParts()
    }

    // ── Load / Search Parts ───────────────────────────
    @OptIn(FlowPreview::class)
    private fun loadParts() {
        viewModelScope.launch {
            // Query change par debounce — 300ms wait karo
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    searchPartsUseCase(
                        query    = query,
                        category = _selectedCategory.value
                    ).collect { result ->
                        _partsState.value = result
                    }
                }
        }
    }

    // ── Update Search Query ───────────────────────────
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    // ── Update Category ───────────────────────────────
    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
        viewModelScope.launch {
            searchPartsUseCase(
                query    = _searchQuery.value,
                category = category
            ).collect { result ->
                _partsState.value = result
            }
        }
    }

    // ── Select Part → Load Vendors ────────────────────
    fun onPartSelect(part: Part) {
        _selectedPart.value = part
        loadVendors(part.id)
    }

    // ── Load Vendors ──────────────────────────────────
    private fun loadVendors(partId: String) {
        viewModelScope.launch {
            _vendorsState.value = AuthResult.Loading
            val result = getVendorsForPartUseCase(partId)

            // Sort apply karo
            _vendorsState.value = when (result) {
                is AuthResult.Success -> {
                    val sorted = sortVendors(
                        result.data,
                        _sortOption.value
                    )
                    AuthResult.Success(sorted)
                }
                else -> result
            }
        }
    }

    // ── Sort Vendors ──────────────────────────────────
    fun onSortChange(option: SortOption) {
        _sortOption.value = option
        val current = _vendorsState.value
        if (current is AuthResult.Success) {
            _vendorsState.value = AuthResult.Success(
                sortVendors(current.data, option)
            )
        }
    }

    private fun sortVendors(
        vendors : List<Vendor>,
        option  : SortOption
    ): List<Vendor> = when (option) {
        SortOption.PRICE    -> vendors.sortedBy { it.price }
        SortOption.NEAREST  -> vendors.sortedBy { it.distance }
        SortOption.RATING   -> vendors.sortedByDescending { it.rating }
    }

    fun resetVendors() {
        _vendorsState.value = null
        _selectedPart.value = null
    }
}

enum class SortOption(val label: String) {
    PRICE   ("Lowest Price"),
    NEAREST ("Nearest"),
    RATING  ("Highest Rating")
}