package com.example.rssreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController

// for dark mode
import androidx.compose.material.icons.filled.Menu
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
                            AppNavHost(navController)
                        }
                    }
                }
            }
        }
    }
}