package com.myawesome.kotlinpa2026.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BlackDarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    background = Color(0xFF000000),
    surface = Color(0xFF121212),
    surfaceVariant = Color(0xFF1E1E1E),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
)

@Composable
fun AppTheme(isDark: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isDark) BlackDarkColorScheme else lightColorScheme(),
        content = content
    )
}
