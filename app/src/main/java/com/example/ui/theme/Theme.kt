package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.model.ReadingTheme

data class BookThemeColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val accent: Color,
    val border: Color,
    val isDark: Boolean
)

fun getThemeColors(theme: ReadingTheme): BookThemeColors {
    return when (theme) {
        ReadingTheme.PARCHMENT -> BookThemeColors(
            background = ParchmentBg,
            surface = ParchmentSurface,
            surfaceVariant = ParchmentSurfaceVariant,
            textPrimary = ParchmentInk,
            textSecondary = ParchmentInkSubtle,
            accent = ParchmentGold,
            border = ParchmentBorder,
            isDark = false
        )
        ReadingTheme.NIGHT -> BookThemeColors(
            background = NightBg,
            surface = NightSurface,
            surfaceVariant = NightSurfaceVariant,
            textPrimary = NightText,
            textSecondary = NightTextSubtle,
            accent = NightGold,
            border = NightBorder,
            isDark = true
        )
        ReadingTheme.LIGHT -> BookThemeColors(
            background = LightBg,
            surface = LightSurface,
            surfaceVariant = LightSurfaceVariant,
            textPrimary = LightText,
            textSecondary = LightTextSubtle,
            accent = LightAccent,
            border = LightBorder,
            isDark = false
        )
    }
}

private val DarkColorScheme = darkColorScheme(
    primary = NightGold,
    secondary = GoldLight,
    background = NightBg,
    surface = NightSurface,
    onBackground = NightText,
    onSurface = NightText
)

private val LightColorScheme = lightColorScheme(
    primary = CrimsonBurgundy,
    secondary = GoldPrimary,
    background = ParchmentBg,
    surface = ParchmentSurface,
    onBackground = ParchmentInk,
    onSurface = ParchmentInk
)

@Composable
fun MyApplicationTheme(
    readingTheme: ReadingTheme = ReadingTheme.PARCHMENT,
    content: @Composable () -> Unit
) {
    val themeColors = getThemeColors(readingTheme)
    val colorScheme = if (themeColors.isDark) {
        darkColorScheme(
            primary = themeColors.accent,
            background = themeColors.background,
            surface = themeColors.surface,
            surfaceVariant = themeColors.surfaceVariant,
            onBackground = themeColors.textPrimary,
            onSurface = themeColors.textPrimary
        )
    } else {
        lightColorScheme(
            primary = themeColors.accent,
            background = themeColors.background,
            surface = themeColors.surface,
            surfaceVariant = themeColors.surfaceVariant,
            onBackground = themeColors.textPrimary,
            onSurface = themeColors.textPrimary
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
