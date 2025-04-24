package com.example.rssreader.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.isSystemInDarkTheme

object AppTheme {
    enum class Theme {
        Light, Dark, System
    }

    @Composable
    operator fun invoke(
        theme: Theme = Theme.System,
        content: @Composable () -> Unit) {
        val isDarkTheme = when (theme) {
            Theme.Light -> false
            Theme.Dark -> true
            Theme.System -> isSystemInDarkTheme()
        }

        MaterialTheme(
            colorScheme = if (isDarkTheme) {
                darkColorScheme()
            } else {
                lightColorScheme()
            },
            content = content
        )
    }
}