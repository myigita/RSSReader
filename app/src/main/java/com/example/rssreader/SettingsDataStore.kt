package com.example.rssreader

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import com.example.rssreader.data.FeedSource
import com.example.rssreader.data.CustomFeed

val Context.settingsDataStore by preferencesDataStore(name = "settings")

object SettingsKeys {
    val darkMode = booleanPreferencesKey("dark_mode")
    val enableThumbnails = booleanPreferencesKey("enable_thumbnails")
    val feedSources = stringPreferencesKey("feed_sources_json")
    val customFeeds = stringPreferencesKey("custom_feeds_json")
}

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

    suspend fun saveFeedSources(sources: List<FeedSource>) {
        val json = Json.encodeToString(sources)
        context.settingsDataStore.edit { it[SettingsKeys.feedSources] = json }
    }

    fun getFeedSources(): Flow<List<FeedSource>> = context.settingsDataStore.data.map {
        val json = it[SettingsKeys.feedSources] ?: "[]"
        try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveCustomFeeds(feeds: List<CustomFeed>) {
        val json = Json.encodeToString(feeds)
        context.settingsDataStore.edit { it[SettingsKeys.customFeeds] = json }
    }

    fun getCustomFeeds(): Flow<List<CustomFeed>> = context.settingsDataStore.data.map {
        val json = it[SettingsKeys.customFeeds] ?: "[]"
        try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
