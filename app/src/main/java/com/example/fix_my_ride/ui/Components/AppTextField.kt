package com.example.fix_my_ride.ui.Components

// ui/components/AppTextField.kt

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    supportingText: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    isError: Boolean = false
) {
    OutlinedTextField(
        value               = value,
        onValueChange       = onValueChange,
        label               = { Text(label) },
        placeholder         = { Text(placeholder, color = MaterialTheme.colorScheme.outline) },
        leadingIcon         = leadingIcon?.let {
            { Icon(it, contentDescription = null) }
        },
        trailingIcon        = trailingIcon?.let {
            {
                IconButton(onClick = { onTrailingIconClick?.invoke() }) {
                    Icon(it, contentDescription = null)
                }
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions     = keyboardOptions,
        keyboardActions     = keyboardActions,
        supportingText      = supportingText,
        enabled             = enabled,
        singleLine          = singleLine,
        isError             = isError,
        shape               = RoundedCornerShape(12.dp),
        modifier            = modifier
    )
}