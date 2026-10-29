package com.example.sonora.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// O Sonora usa sempre o tema escuro, com cores próprias (sem cor dinâmica do sistema)
private val SonoraColorScheme = darkColorScheme(
    primary = SonoraPink,
    onPrimary = Color.White,
    primaryContainer = SonoraPinkContainer,
    onPrimaryContainer = Color(0xFFFFD9E2),
    secondary = SonoraViolet,
    onSecondary = Color.White,
    secondaryContainer = SonoraVioletContainer,
    onSecondaryContainer = Color(0xFFE6DEFF),
    tertiary = SonoraTeal,
    onTertiary = Color.Black,
    background = SonoraBackground,
    onBackground = SonoraOnSurface,
    surface = SonoraSurface,
    onSurface = SonoraOnSurface,
    surfaceVariant = SonoraSurfaceVariant,
    onSurfaceVariant = SonoraOnSurfaceVariant,
    surfaceContainer = SonoraSurfaceContainer,
    surfaceContainerHigh = SonoraSurfaceVariant,
    outline = SonoraOutline,
    outlineVariant = SonoraOutline,
    error = SonoraError,
)

@Composable
fun SonoraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SonoraColorScheme,
        typography = Typography,
        content = content
    )
}
