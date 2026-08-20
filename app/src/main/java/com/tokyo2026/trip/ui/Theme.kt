package com.tokyo2026.trip.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Sakura = Color(0xFFE8607A)
private val Indigo = Color(0xFF1E3A5F)
private val Gold = Color(0xFFE0A32E)

private val LightColors = lightColorScheme(
    primary = Indigo,
    onPrimary = Color.White,
    secondary = Sakura,
    onSecondary = Color.White,
    tertiary = Gold,
    background = Color(0xFFFBF7F1),
    onBackground = Color(0xFF1B1A18),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1B1A18),
    surfaceVariant = Color(0xFFEFE7DA),
    onSurfaceVariant = Color(0xFF4A453D)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9EC1F0),
    onPrimary = Color(0xFF0B1B2E),
    secondary = Color(0xFFFF97AA),
    onSecondary = Color(0xFF3A0713),
    tertiary = Gold,
    background = Color(0xFF14161A),
    onBackground = Color(0xFFECE6DE),
    surface = Color(0xFF1D2026),
    onSurface = Color(0xFFECE6DE),
    surfaceVariant = Color(0xFF2A2F38),
    onSurfaceVariant = Color(0xFFC7C1B8)
)

@Composable
fun Tokyo2026Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = Typography(),
        content = content
    )
}
