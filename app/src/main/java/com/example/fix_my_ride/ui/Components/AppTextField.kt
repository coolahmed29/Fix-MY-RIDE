package com.example.fix_my_ride.ui.Components

// ui/components/AppTextField.kt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.fix_my_ride.ui.theme.IconColor
import com.example.fix_my_ride.ui.theme.InputBg
import com.example.fix_my_ride.ui.theme.Primary
import com.example.fix_my_ride.ui.theme.TextPlaceholder
import com.example.fix_my_ride.ui.theme.TextPrimary

@Composable
fun AppTextField(
    value                : String,
    onValueChange        : (String) -> Unit,
    label                : String,
    modifier             : Modifier = Modifier,
    placeholder          : String = "",
    leadingIcon          : ImageVector? = null,
    trailingIcon         : ImageVector? = null,
    onTrailingIconClick  : (() -> Unit)? = null,
    visualTransformation : VisualTransformation = VisualTransformation.None,
    keyboardOptions      : KeyboardOptions = KeyboardOptions.Default,
    keyboardActions      : KeyboardActions = KeyboardActions.Default,
    supportingText       : @Composable (() -> Unit)? = null,
    enabled              : Boolean = true,
    singleLine           : Boolean = true,
    isError              : Boolean = false
) {
    Box(
        modifier = modifier
            .shadow(
                elevation    = 4.dp,
                shape        = RoundedCornerShape(25.dp),
                ambientColor = Color.Gray.copy(alpha = 0.1f),
                spotColor    = Color.Gray.copy(alpha = 0.1f)
            )
            .clip(RoundedCornerShape(25.dp))
            .background(InputBg)
    ) {
        TextField(
            value                = value,
            onValueChange        = onValueChange,
            placeholder          = {
                Text(
                    text  = placeholder,
                    color = TextPlaceholder
                )
            },
            label                = {
                Text(
                    text  = label,
                    color = TextPlaceholder
                )
            },
            leadingIcon          = leadingIcon?.let {
                {
                    Icon(
                        imageVector        = it,
                        contentDescription = null,
                        tint               = IconColor
                    )
                }
            },
            trailingIcon         = trailingIcon?.let {
                {
                    IconButton(
                        onClick = { onTrailingIconClick?.invoke() }
                    ) {
                        Icon(
                            imageVector        = it,
                            contentDescription = null,
                            tint               = IconColor
                        )
                    }
                }
            },
            visualTransformation = visualTransformation,
            keyboardOptions      = keyboardOptions,
            keyboardActions      = keyboardActions,
            supportingText       = supportingText,
            enabled              = enabled,
            singleLine           = singleLine,
            isError              = isError,
            modifier             = Modifier.fillMaxWidth(),
            colors               = TextFieldDefaults.colors(
                focusedContainerColor   = InputBg,
                unfocusedContainerColor = InputBg,
                disabledContainerColor  = InputBg,
                focusedIndicatorColor   = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor  = Color.Transparent,
                errorIndicatorColor     = Color.Transparent,
                focusedTextColor        = TextPrimary,
                unfocusedTextColor      = TextPrimary,
                focusedLeadingIconColor = Primary,     // Focus pe green
                cursorColor             = Primary
            )
        )
    }
}