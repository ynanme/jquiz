package com.example.jquiz.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val BlueLightScheme = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color.White,
    secondary = Color(0xFF5E92F3),
    onSecondary = Color.White,
    background = Color(0xFFF5F9FF),
    onBackground = Color(0xFF0D1C2C),
    surface = Color.White,
    onSurface = Color(0xFF0D1C2C),
    error = Color(0xFFB00020),
    onError = Color.White
)

val BlueDarkScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color.Black,
    secondary = Color(0xFF64B5F6),
    onSecondary = Color.Black,
    background = Color(0xFF0D1B2A),
    onBackground = Color(0xFFE3F2FD),
    surface = Color(0xFF1B263B),
    onSurface = Color(0xFFE3F2FD),
    error = Color(0xFFCF6679),
    onError = Color.Black
)


val GreenLightScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color.White,
    secondary = Color(0xFF66BB6A),
    onSecondary = Color.White,
    background = Color(0xFFF1F8F4),
    onBackground = Color(0xFF0D1C0D),
    surface = Color.White,
    onSurface = Color(0xFF0D1C0D),
    error = Color(0xFFB00020),
    onError = Color.White
)

val GreenDarkScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color.Black,
    secondary = Color(0xFF66BB6A),
    onSecondary = Color.Black,
    background = Color(0xFF0D1B12),
    onBackground = Color(0xFFE8F5E9),
    surface = Color(0xFF1B3520),
    onSurface = Color(0xFFE8F5E9),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun JQuizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}