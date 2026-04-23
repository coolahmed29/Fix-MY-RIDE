package com.example.fix_my_ride.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary          = Primary,
    secondary        = Secondary,
    background       = Background,
    surface          = Surface,
    onPrimary        = Surface,          // White text on blue button
    onSecondary      = Surface,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    error            = ErrorRed,
    onError          = Surface,
    secondaryContainer = Secondary.copy(alpha = 0.15f),
    surfaceVariant   = Background
)

private val DarkColorScheme = darkColorScheme(
    primary          = DarkPrimary,
    secondary        = DarkSecondary,
    background       = DarkBackground,
    surface          = DarkSurface,
    onPrimary        = DarkBackground,
    onSecondary      = DarkBackground,
    onBackground     = DarkTextPrimary,
    onSurface        = DarkTextPrimary,
    error            = ErrorRed,
    onError          = Surface,
    secondaryContainer = DarkSecondary.copy(alpha = 0.15f),
    surfaceVariant   = DarkSurface
)

@Composable
fun FixMyRideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = AppTypography,
        content     = content
    )
}

//@Composable
//fun FixMYrideTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit
//) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}