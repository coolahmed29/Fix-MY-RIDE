// ui/theme/Theme.kt
package com.example.fix_my_ride.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary             = Primary,
    onPrimary           = CardBg,
    primaryContainer    = PrimaryLight,
    onPrimaryContainer  = PrimaryDark,
    secondary           = PrimaryLight,
    onSecondary         = CardBg,
    background          = Background,
    onBackground        = TextPrimary,
    surface             = CardBg,
    onSurface           = TextPrimary,
    surfaceVariant      = InputBg,
    onSurfaceVariant    = TextSecondary,
    error               = ErrorRed,
    onError             = CardBg,
    outline             = TextPlaceholder
)

private val DarkColorScheme = darkColorScheme(
    primary             = DarkPrimary,
    onPrimary           = DarkBackground,
    primaryContainer    = DarkPrimaryDark,
    onPrimaryContainer  = DarkTextPrimary,
    background          = DarkBackground,
    onBackground        = DarkTextPrimary,
    surface             = DarkSurface,
    onSurface           = DarkTextPrimary,
    surfaceVariant      = DarkInputBg,
    onSurfaceVariant    = DarkTextSecondary,
    error               = ErrorRed,
    onError             = CardBg,
    outline             = DarkTextSecondary
)

@Composable
fun FixMyRideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme
    else           LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = AppTypography,
        content     = content
    )
}