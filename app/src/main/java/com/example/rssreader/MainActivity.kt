package com.example.rssreader

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Refresh
import androidx.core.net.toUri
import androidx.compose.ui.platform.LocalContext

// for dark mode
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.app.Activity
import androidx.compose.ui.platform.LocalView


class MainActivity: ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // items is a list of FeedItem of the RSS feed
            // isLoading is a boolean that indicates if the feed is loading
            val context = LocalContext.current
            val settingsStore = remember { SettingsDataStore(context) }
            val isDarkMode by settingsStore.isDarkMode.collectAsState(initial = false)

            val items = remember { mutableStateListOf<FeedItem>() }
            var isLoading by remember { mutableStateOf(true) }

            val view = LocalView.current

            SideEffect {
                val window = (context as Activity).window
                WindowCompat.setDecorFitsSystemWindows(window, false)
                val insetsController = WindowInsetsControllerCompat(window, view)
                insetsController.isAppearanceLightStatusBars = !isDarkMode
            }


            LaunchedEffect(Unit) {
                isLoading = true
                val result = fetchRss()
                items.clear()
                items.addAll(result)
                isLoading = false
            }

            MaterialTheme(
                colorScheme = if (isDarkMode) {
                    darkColorScheme()
                } else {
                    lightColorScheme()
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("RSS Reader") },
                            actions = {
                                IconButton(onClick = {
                                    // toggle dark mode
                                    lifecycleScope.launch {
                                        settingsStore.setDarkMode(!isDarkMode)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Brightness6,
                                        contentDescription = "Toggle Dark Mode"
                                    )
                                }
                            }
                        )
                    },
                    content = { padding ->
                        Box(modifier = Modifier.fillMaxSize()) {

                            // RSS list
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                itemsIndexed(items) { index, item ->
                                    FeedItemCard(item) {
                                        items[index] = item.copy(isRead = true)
                                        val intent = Intent(Intent.ACTION_VIEW, item.link.toUri())
                                        startActivity(intent)
                                    }
                                }
                            }

                            // Bottom-left floating refresh/progress
                            IconButton(
                                onClick = {
                                    isLoading = true
                                    lifecycleScope.launch {
                                        val result = fetchRss()
                                        items.clear()
                                        items.addAll(result)
                                        isLoading = false
                                    }
                                },
                                modifier = Modifier.align(Alignment.BottomEnd)
                                    .padding(24.dp)
                                    .size(56.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}