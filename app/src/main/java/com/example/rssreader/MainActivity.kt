package com.example.rssreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import kotlin.collections.listOf

// for dark mode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.unit.dp
import com.example.rssreader.data.CustomFeed
import com.example.rssreader.ui.AppNavHost
import com.example.rssreader.ui.theme.AppTheme


class MainActivity: ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val settingsStore = remember { SettingsDataStore(context) }
            val isDarkMode by settingsStore.isDarkMode.collectAsState(initial = false)
            val navController = rememberNavController()

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            var feedsExpanded by remember { mutableStateOf(false) }
            val customCustomFeeds = remember {
                listOf(
                    CustomFeed(title = "Home")
                )
            }

            val allFeedSources by settingsStore.getFeedSources().collectAsState(initial = emptyList())
            val customFeeds by settingsStore.getCustomFeeds().collectAsState(initial = listOf(CustomFeed(title = "Home")))

            AppTheme(theme = if (isDarkMode) AppTheme.Theme.Dark else AppTheme.Theme.Light) {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            TextButton(onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("home")
                            }) {
                                Text("Home")
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            )

                            TextButton(onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("feedSources")
                            }) {
                                Text("Manage Feed Sources")
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            )

                            TextButton(onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("customFeeds")
                            }) {
                                Text("Manage Custom Feeds")
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            )

                            Column {
                                TextButton(onClick = { feedsExpanded = !feedsExpanded }) {
                                    Text("Feeds")
                                    Icon(
                                        imageVector = if (feedsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = if (feedsExpanded) "Collapse Feeds" else "Expand Feeds"
                                    )
                                }

                                if (feedsExpanded) {
                                    customCustomFeeds.forEach { feed ->
                                        TextButton(onClick = {
                                            scope.launch { drawerState.close() }
                                            navController.navigate("feed/${feed.title}")
                                        }, modifier = Modifier.padding(start = 16.dp)
                                            ) {
                                            Text(feed.title)
                                        }
                                    }
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            )

                            TextButton(onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("settings")
                            }) {
                                Text("Settings")
                            }
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("RSS Reader") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch { drawerState.open() }
                                    }) {
                                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                                    }
                                }
                            )
                        }
                    ) { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)) {
                            AppNavHost(
                                navController = navController,
                                allFeedSources = allFeedSources,
                                customFeeds = customFeeds,
                                onSaveFeedSources = { updatedSources ->
                                    scope.launch { settingsStore.saveFeedSources(updatedSources) }
                                },
                                onSaveCustomFeeds = { updatedFeeds ->
                                    scope.launch { settingsStore.saveCustomFeeds(updatedFeeds) }
                                }
                            )                        }
                    }
                }
            }
        }
    }
}