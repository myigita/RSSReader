package com.example.rssreader.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rssreader.ui.screens.HomeScreen
import com.example.rssreader.ui.screens.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = "home",
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("home") { HomeScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}
