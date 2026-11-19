package com.Baxwope9.Rufnim.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FestivalColorScheme = darkColorScheme(
    primary = Saffron,
    onPrimary = NavyCanvas,
    primaryContainer = SaffronDeep,
    onPrimaryContainer = NavyDeep,
    secondary = Teal,
    onSecondary = NavyCanvas,
    secondaryContainer = SurfaceElevated,
    onSecondaryContainer = Teal,
    tertiary = Hazard,
    onTertiary = TextPrimary,
    background = NavyCanvas,
    onBackground = TextPrimary,
    surface = SurfacePanel,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = Hairline,
    outlineVariant = Hairline,
    error = Hazard,
    onError = TextPrimary
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FestivalColorScheme,
        typography = AppTypography,
        content = content
    )
}
