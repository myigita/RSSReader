package com.example.rssreader.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rssreader.data.CustomFeed
import com.example.rssreader.data.FeedSource

@Composable
fun CustomFeedCard(
    feed: CustomFeed,
    feedSources: List<FeedSource>,
    onUpdate: (CustomFeed) -> Unit,
    onDelete: () -> Unit
) {
    var name by remember { mutableStateOf(feed.title) }
    var expression by remember { mutableStateOf(feed.expression) }
    val included = remember { mutableStateListOf<String>().apply { addAll(feed.includedFeedSourceIds) } }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    onUpdate(feed.copy(title = name, expression = expression, includedFeedSourceIds = included.toList()))
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = expression,
                onValueChange = {
                    expression = it
                    onUpdate(feed.copy(title = name, expression = expression, includedFeedSourceIds = included.toList()))
                },
                label = { Text("Tag Expression") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Include these sources manually:", style = MaterialTheme.typography.labelMedium)

            feedSources.forEach { source ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = included.contains(source.id),
                        onCheckedChange = {
                            if (it) included.add(source.id) else included.remove(source.id)
                            onUpdate(feed.copy(title = name, expression = expression, includedFeedSourceIds = included.toList()))
                        }
                    )
                    Text(source.name)
                }
            }

            TextButton(onClick = onDelete) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
