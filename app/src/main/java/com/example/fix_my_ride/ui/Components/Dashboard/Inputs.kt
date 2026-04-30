package com.example.fix_my_ride.ui.Components.Dashboard


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fix_my_ride.ui.Components.Dashboard.AppColors
import com.example.fix_my_ride.ui.Components.Dashboard.Radius
import com.example.fix_my_ride.ui.theme.Roboto

// ── Reusable text field for any dialog/form ────────────────────────────────────
@Composable
fun DsTextField(
    value         : String,
    onValueChange : (String) -> Unit,
    label         : String,
    modifier      : Modifier = Modifier,
    keyboardType  : KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value           = value,
        onValueChange   = onValueChange,
        label           = { Text(label, fontFamily = Roboto, fontSize = 12.sp) },
        modifier        = modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(Radius.SM.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine      = true,
        colors          = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = AppColors.Primary,
            unfocusedBorderColor = Color(0xFFE0E0E0)
        )
    )
}