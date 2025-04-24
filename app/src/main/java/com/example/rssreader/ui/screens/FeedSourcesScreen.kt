package com.example.rssreader.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rssreader.data.FeedSource
import com.example.rssreader.ui.components.FeedSourceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedSourcesScreen(
    navController: NavController,
    feedSources: List<FeedSource>,
    onSave: (List<FeedSource>) -> Unit
) {
    val sources = remember { mutableStateListOf<FeedSource>().apply { addAll(feedSources) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feed Sources") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onSave(sources)
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
        ) {
            sources.forEachIndexed { index, source ->
                FeedSourceCard(
                    feedSource = source,
                    onDelete = { sources.removeAt(index) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                sources.add(FeedSource(name = "", url = ""))
            }) {
                Text("Add Feed Source")
            }
        }
    }
}
