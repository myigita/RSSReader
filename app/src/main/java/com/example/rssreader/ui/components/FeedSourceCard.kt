package com.example.rssreader.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.rssreader.data.FeedSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

@Composable
fun FeedSourceCard(
    feedSource: FeedSource,
    onDelete: () -> Unit
) {
    var name by remember { mutableStateOf(feedSource.name) }
    var url by remember { mutableStateOf(feedSource.url) }
    var checkStatus by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("URL") }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    scope.launch {
                        checkStatus = "Checking..."
                        checkStatus = checkFeedUrl(url)
                    }
                }) {
                    Text("Check")
                }

                TextButton(onClick = onDelete) {
                    Text("Delete")
                }
            }

            checkStatus?.let {
                Text(
                    text = it,
                    color = when {
                        it.contains("✅") -> MaterialTheme.colorScheme.primary
                        it.contains("❌") -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }

    feedSource.name = name
    feedSource.url = url
}

suspend fun checkFeedUrl(url: String): String = withContext(Dispatchers.IO) {
    return@withContext try {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful && response.body?.string()?.contains("<rss") == true) {
            "✅ RSS Feed Found"
        } else {
            "❌ Not a valid RSS feed"
        }
    } catch (e: Exception) {
        "❌ Failed to connect: ${e.localizedMessage}"
    }
}