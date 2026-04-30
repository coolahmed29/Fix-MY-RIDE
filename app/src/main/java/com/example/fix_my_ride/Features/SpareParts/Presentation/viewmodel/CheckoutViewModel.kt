package com.example.fix_my_ride.Features.SpareParts.Presentation.viewmodel


// Presentation/CheckoutViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Order
import com.example.fix_my_ride.Features.SpareParts.Domain.model.OrderStatus
import com.example.fix_my_ride.Features.SpareParts.Domain.model.PaymentMethod
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Part
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Vendor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

// ── UI State ─────────────────────────────────────────────────────────────────
data class CheckoutUiState(
    val buyerName     : String        = "",
    val buyerPhone    : String        = "",
    val buyerAddress  : String        = "",
    val quantity      : Int           = 1,
    val paymentMethod : PaymentMethod = PaymentMethod.CASH_ON_DELIVERY,

    // Validation
    val nameError    : String? = null,
    val phoneError   : String? = null,
    val addressError : String? = null,

    // Step tracking
    val currentStep  : CheckoutStep   = CheckoutStep.DETAILS,

    // Submission
    val isLoading    : Boolean = false,
    val placedOrder  : Order?  = null,
    val error        : String? = null
)

enum class CheckoutStep { DETAILS, REVIEW, DONE }

// ── ViewModel ─────────────────────────────────────────────────────────────────
class CheckoutViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    // ── Field updates ─────────────────────────────────────────────────────────
    fun onNameChange(v: String)    = _uiState.update { it.copy(buyerName    = v, nameError    = null) }
    fun onPhoneChange(v: String)   = _uiState.update { it.copy(buyerPhone   = v, phoneError   = null) }
    fun onAddressChange(v: String) = _uiState.update { it.copy(buyerAddress = v, addressError = null) }

    fun onQuantityChange(q: Int) {
        if (q < 1) return
        _uiState.update { it.copy(quantity = q) }
    }

    fun onPaymentMethodChange(m: PaymentMethod) =
        _uiState.update { it.copy(paymentMethod = m) }

    // ── Navigation ────────────────────────────────────────────────────────────
    fun proceedToReview() {
        if (!validate()) return
        _uiState.update { it.copy(currentStep = CheckoutStep.REVIEW) }
    }

    fun goBackToDetails() =
        _uiState.update { it.copy(currentStep = CheckoutStep.DETAILS) }

    // ── Place Order (simulated) ───────────────────────────────────────────────
    fun placeOrder(part: Part, vendor: Vendor) {
        val s = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            delay(1800) // Simulate network call

            val order = Order(
                id            = UUID.randomUUID().toString().take(8).uppercase(),
                partId        = part.id,
                partName      = part.name,
                vendorId      = vendor.id,
                vendorName    = vendor.name,
                vendorAddress = vendor.address,
                quantity      = s.quantity,
                pricePerUnit  = vendor.price,
                totalPrice    = vendor.price * s.quantity,
                buyerName     = s.buyerName,
                buyerPhone    = s.buyerPhone,
                buyerAddress  = s.buyerAddress,
                paymentMethod = s.paymentMethod,
                status        = OrderStatus.CONFIRMED,
                placedAt      = System.currentTimeMillis()
            )

            _uiState.update {
                it.copy(
                    isLoading   = false,
                    placedOrder = order,
                    currentStep = CheckoutStep.DONE
                )
            }
        }
    }

    fun resetCheckout() = _uiState.update { CheckoutUiState() }

    // ── Validation ────────────────────────────────────────────────────────────
    private fun validate(): Boolean {
        val s = _uiState.value
        var valid = true

        val nameErr = when {
            s.buyerName.isBlank()    -> "Name is required"
            s.buyerName.length < 3   -> "Enter a valid name"
            else                     -> null
        }
        val phoneErr = when {
            s.buyerPhone.isBlank()               -> "Phone number is required"
            !s.buyerPhone.matches(Regex("^03\\d{9}$")) -> "Enter valid Pakistani number (03XXXXXXXXX)"
            else                                  -> null
        }
        val addressErr = when {
            s.buyerAddress.isBlank()  -> "Delivery address is required"
            s.buyerAddress.length < 10 -> "Please enter a more detailed address"
            else                       -> null
        }

        if (nameErr != null || phoneErr != null || addressErr != null) valid = false

        _uiState.update {
            it.copy(
                nameError    = nameErr,
                phoneError   = phoneErr,
                addressError = addressErr
            )
        }
        return valid
    }
}