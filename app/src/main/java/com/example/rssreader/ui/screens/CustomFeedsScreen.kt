package com.example.rssreader.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rssreader.data.CustomFeed
import com.example.rssreader.data.FeedSource
import com.example.rssreader.ui.components.CustomFeedCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFeedsScreen(
    navController: NavController,
    customFeeds: List<CustomFeed>,
    feedSources: List<FeedSource>,
    onSave: (List<CustomFeed>) -> Unit
) {
    val feeds = remember { mutableStateListOf<CustomFeed>().apply { addAll(customFeeds) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Custom Feeds") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onSave(feeds)
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(feeds) { index, feed ->
                    CustomFeedCard(
                        feed = feed,
                        feedSources = feedSources,
                        onUpdate = { updated -> feeds[index] = updated },
                        onDelete = { feeds.removeAt(index) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Button(
                onClick = {
                    feeds.add(CustomFeed(title = "", expression = ""))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Custom Feed")
            }
        }
    }
}
