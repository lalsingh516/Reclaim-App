package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ReclaimColorScheme = lightColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryLight,
    secondary = ReclaimIndigo,
    onSecondary = OnPrimaryLight,
    background = ReclaimBackground,
    onBackground = TextDark,
    surface = CardBackground,
    onSurface = TextDark,
    surfaceVariant = ReclaimSurface,
    onSurfaceVariant = TextGray,
    outline = BorderGray
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ReclaimColorScheme,
        typography = Typography,
        content = content
    )
}
