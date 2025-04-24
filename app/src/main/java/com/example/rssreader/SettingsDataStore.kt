package com.example.rssreader

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// 1. Create the DataStore instance
val Context.settingsDataStore by preferencesDataStore(name = "settings")

// 2. Keys for settings
object SettingsKeys {
    val darkMode = booleanPreferencesKey("dark_mode")
    val enableThumbnails = booleanPreferencesKey("enable_thumbnails")
    val feedSources = stringPreferencesKey("feed_sources_json")
}

// 3. Helper to read/write
class SettingsDataStore(private val context: Context) {

    val isDarkMode: Flow<Boolean> = context.settingsDataStore.data
        .map { preferences ->
            preferences[SettingsKeys.darkMode] ?: false
        }

    suspend fun setDarkMode(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.darkMode] = enabled
        }
    }

    val enableThumbnails: Flow<Boolean> = context.settingsDataStore.data
        .map { preferences ->
            preferences[SettingsKeys.enableThumbnails] ?: true
        }

    suspend fun setEnableThumbnails(enabled: Boolean) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.enableThumbnails] = enabled
        }
    }
}
