package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EchoBridgeColorScheme = darkColorScheme(
    primary = EchoBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = EchoTextPrimary,
    secondary = EchoPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF3B1F70),
    onSecondaryContainer = EchoTextPrimary,
    tertiary = EchoCyan,
    onTertiary = Color(0xFF04050D),
    background = EchoBgDeep,
    onBackground = EchoTextPrimary,
    surface = EchoBgSurface,
    onSurface = EchoTextPrimary,
    surfaceVariant = EchoBgElevated,
    onSurfaceVariant = EchoTextSecondary,
    outline = EchoCardBorder,
    error = EchoCoral,
    onError = Color.White
)

@Composable
fun EchoBridgeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EchoBridgeColorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    EchoBridgeTheme(content = content)
}
