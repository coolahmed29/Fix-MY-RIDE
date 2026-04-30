package com.example.fix_my_ride.Features.SpareParts.Presentation.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Part
import com.example.fix_my_ride.Features.SpareParts.Domain.model.PaymentMethod
import com.example.fix_my_ride.Features.SpareParts.Domain.model.Vendor
import com.example.fix_my_ride.Features.SpareParts.Presentation.viewmodel.CheckoutStep
import com.example.fix_my_ride.Features.SpareParts.Presentation.viewmodel.CheckoutUiState
import com.example.fix_my_ride.Features.SpareParts.Presentation.viewmodel.CheckoutViewModel
import com.example.fix_my_ride.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    part      : Part,
    vendor    : Vendor,
    onBack    : () -> Unit,
    onFinish  : () -> Unit,
    viewModel : CheckoutViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (state.currentStep) {
                            CheckoutStep.DETAILS -> "Delivery Details"
                            CheckoutStep.REVIEW  -> "Review Order"
                            CheckoutStep.DONE    -> "Order Placed!"
                        },
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 17.sp
                    )
                },
                navigationIcon = {
                    if (state.currentStep != CheckoutStep.DONE) {
                        IconButton(onClick = {
                            if (state.currentStep == CheckoutStep.REVIEW)
                                viewModel.goBackToDetails()
                            else
                                onBack()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DashCardBg)
            )
        },
        containerColor = Color(0xFFF5F6FA)
    ) { padding ->

        AnimatedContent(
            targetState = state.currentStep,
            modifier    = Modifier
                .fillMaxSize()
                .padding(padding),
            transitionSpec = {
                if (targetState.ordinal > initialState.ordinal)
                    slideInHorizontally(tween(300)) { it } togetherWith slideOutHorizontally(tween(300)) { -it }
                else
                    slideInHorizontally(tween(300)) { -it } togetherWith slideOutHorizontally(tween(300)) { it }
            },
            label = "checkout_step"
        ) { step ->
            when (step) {
                CheckoutStep.DETAILS -> DetailsStep(state, viewModel, part, vendor)
                CheckoutStep.REVIEW  -> ReviewStep(state, viewModel, part, vendor)
                CheckoutStep.DONE    -> ConfirmationStep(state) {
                    viewModel.resetCheckout()
                    onFinish()
                }
            }
        }
    }
}

// ── Step 1: Details ───────────────────────────────────────────────────────────
@Composable
private fun DetailsStep(
    state     : CheckoutUiState,
    viewModel : CheckoutViewModel,
    part      : Part,
    vendor    : Vendor
) {
    Column(modifier = Modifier.fillMaxSize().imePadding()) {
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(part.name, fontFamily = Montserrat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text("From: ${vendor.name}", color = DashTextSecondary, fontSize = 12.sp)
                    }
                    Text("Rs ${vendor.price.toInt()}", color = Primary, fontWeight = FontWeight.Bold)
                }
            }

            SectionCard {
                SectionLabel("Quantity")
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    QuantityButton("−") { viewModel.onQuantityChange((state.quantity - 1).coerceAtLeast(1)) }
                    Text(state.quantity.toString(), modifier = Modifier.width(48.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    QuantityButton("+") { viewModel.onQuantityChange(state.quantity + 1) }
                    Spacer(Modifier.weight(1f))
                    Text("Total: Rs ${(vendor.price * state.quantity).toInt()}", color = Primary, fontWeight = FontWeight.SemiBold)
                }
            }

            SectionCard {
                SectionLabel("Your Information")
                Spacer(Modifier.height(8.dp))
                CheckoutTextField(state.buyerName, viewModel::onNameChange, "Full Name", Icons.Default.Person, state.nameError)
                CheckoutTextField(state.buyerPhone, viewModel::onPhoneChange, "Phone", Icons.Default.Phone, state.phoneError, KeyboardType.Phone)
                CheckoutTextField(state.buyerAddress, viewModel::onAddressChange, "Address", Icons.Default.Place, state.addressError, minLines = 3)
            }

            SectionCard {
                SectionLabel("Payment Method")
                PaymentMethod.values().forEach { method ->
                    PaymentOption(method, state.paymentMethod == method) { viewModel.onPaymentMethodChange(method) }
                }
            }
        }

        Button(
            onClick = { viewModel.proceedToReview() },
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Text("Review Order", fontFamily = Montserrat, fontWeight = FontWeight.Bold)
        }
    }
}

// ── Step 2: Review ────────────────────────────────────────────────────────────
@Composable
private fun ReviewStep(
    state: CheckoutUiState,
    viewModel: CheckoutViewModel,
    part: Part,
    vendor: Vendor
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionCard {
                SectionLabel("Order Summary")
                ReviewRow("Part", part.name)
                ReviewRow("Vendor", vendor.name)
                ReviewRow("Quantity", state.quantity.toString())
                HorizontalDivider(Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(0.3f))
                ReviewRow("Total Amount", "Rs ${(vendor.price * state.quantity).toInt()}", bold = true, valueColor = Primary)
            }

            SectionCard {
                SectionLabel("Delivery Details")
                ReviewRow("Name", state.buyerName)
                ReviewRow("Phone", state.buyerPhone)
                ReviewRow("Address", state.buyerAddress)
                ReviewRow("Payment", state.paymentMethod.name.replace("_", " "))
            }
        }

        Button(
            onClick = { viewModel.placeOrder(part, vendor) },
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(54.dp),
            enabled = !state.isLoading,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            if (state.isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("Confirm & Place Order", fontFamily = Montserrat, fontWeight = FontWeight.Bold)
        }
    }
}

// ── Step 3: Confirmation ──────────────────────────────────────────────────────
@Composable
private fun ConfirmationStep(state: CheckoutUiState, onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.CheckCircle, null, tint = IconGreen, modifier = Modifier.size(100.dp))
        Spacer(Modifier.height(16.dp))
        Text("Order Placed!", fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = Montserrat)
        Text("Your order has been sent to the vendor.", textAlign = TextAlign.Center, color = DashTextSecondary)
        Spacer(Modifier.height(32.dp))
        Button(onClick = onFinish, modifier = Modifier.fillMaxWidth(0.7f), colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
            Text("Back to Home")
        }
    }
}

// ── Helper Components ────────────────────────────────────────────────────────
@Composable
fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White).padding(16.dp), content = content)
}

@Composable
fun SectionLabel(text: String) {
    Text(text, fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DashTextPrimary)
}

@Composable
fun ReviewRow(label: String, value: String, bold: Boolean = false, valueColor: Color = DashTextPrimary) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = DashTextLight, fontSize = 14.sp)
        Text(value, fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium, color = valueColor, fontSize = 14.sp)
    }
}

@Composable
fun CheckoutTextField(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector, error: String?, keyboardType: KeyboardType = KeyboardType.Text, minLines: Int = 1) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange, label = { Text(label) },
        leadingIcon = { Icon(icon, null, Modifier.size(20.dp), tint = Primary) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        isError = error != null, shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        minLines = minLines, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Primary)
    )
}

@Composable
fun QuantityButton(label: String, onClick: () -> Unit) {
    Box(Modifier.size(36.dp).clip(CircleShape).background(Primary.copy(0.1f)).clickable { onClick() }.border(1.dp, Primary.copy(0.2f), CircleShape), contentAlignment = Alignment.Center) {
        Text(label, color = Primary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

@Composable
fun PaymentOption(method: PaymentMethod, selected: Boolean, onSelect: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp).clip(RoundedCornerShape(10.dp))
            .background(if (selected) Primary.copy(0.05f) else Color.Transparent)
            .border(1.dp, if (selected) Primary else Color.LightGray.copy(0.2f), RoundedCornerShape(10.dp))
            .clickable { onSelect() }.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(if (method == PaymentMethod.CASH_ON_DELIVERY) Icons.Default.LocalShipping else Icons.Default.ShoppingCart, null, tint = if (selected) Primary else Color.Gray)
        Spacer(Modifier.width(12.dp))
        Text(method.name.replace("_", " "), color = if (selected) DashTextPrimary else Color.Gray)
    }
}