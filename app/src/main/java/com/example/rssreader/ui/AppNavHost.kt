package com.example.rssreader.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rssreader.data.CustomFeed
import com.example.rssreader.data.FeedSource
import com.example.rssreader.ui.screens.CustomFeedsScreen
import com.example.rssreader.ui.screens.FeedScreen
import com.example.rssreader.ui.screens.FeedSourcesScreen
import com.example.rssreader.ui.screens.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    allFeedSources: List<FeedSource>,
    customFeeds: List<CustomFeed>,
    onSaveFeedSources: (List<FeedSource>) -> Unit,
    onSaveCustomFeeds: (List<CustomFeed>) -> Unit,
    startDestination: String = "feed/Home"
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("home") { FeedScreen(customFeed = CustomFeed("Home"), allSources = allFeedSources) }
        composable("settings") { SettingsScreen(navController) }
        composable("feed/{feedName}") { backStackEntry ->
            val feedName = backStackEntry.arguments?.getString("feedName") ?: "Home"
            val customFeed = customFeeds.find { it.title == feedName } ?: CustomFeed("Home")
            FeedScreen(customFeed = customFeed, allSources = allFeedSources)
        }
        composable("feedSources") {
            FeedSourcesScreen(
                navController = navController,
                feedSources = allFeedSources,
                onSave = onSaveFeedSources
            )
        }
        composable("customFeeds") {
            CustomFeedsScreen(
                navController = navController,
                customFeeds = customFeeds,
                feedSources = allFeedSources,
                onSave = onSaveCustomFeeds
            )
        }
    }
}
