package com.queuemanager.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryTeal,
    onPrimary = Color.White,
    secondary = DarkTeal,
    background = DarkTeal,
    surface = DarkTeal,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryTeal,
    onPrimary = Color.White,
    secondary = DarkTeal,
    onSecondary = Color.White,
    background = BackgroundColor,
    onBackground = DarkTeal,
    surface = SurfaceColor,
    onSurface = DarkTeal,
    surfaceVariant = LightTeal,
    onSurfaceVariant = DarkTeal,
    outline = SlateGrey,
    error = ErrorRed
)

@Composable
fun QueueManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
