package com.example.rssreader.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.example.rssreader.data.CustomFeed
import com.example.rssreader.data.FeedItem
import com.example.rssreader.data.FeedSource
import com.example.rssreader.data.matchesExpression
import com.example.rssreader.fetchRss
import com.example.rssreader.ui.components.FeedItemCard

@Composable
fun FeedScreen(
    customFeed: CustomFeed,
    allSources: List<FeedSource>
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }
    val feedItems = remember { mutableStateListOf<FeedItem>() }

    val sourcesToUse = remember(customFeed, allSources) {
        allSources.filter { it.matchesExpression(customFeed) }
    }

    LaunchedEffect(customFeed.title, sourcesToUse) {
        isLoading = true
        val combined = sourcesToUse.flatMap { fetchRss(it) }
        feedItems.clear()
        feedItems.addAll(combined)
        isLoading = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(feedItems) { item ->
                FeedItemCard(item = item) {
                    item.isRead = true
                    val intent = Intent(Intent.ACTION_VIEW, item.link.toUri())
                    context.startActivity(intent)
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
