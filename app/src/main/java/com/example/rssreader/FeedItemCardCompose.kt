package com.example.rssreader

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun FeedItemCard(
    item: FeedItem,
    onClick: () -> Unit
) {
    val dimmedAlpha = if (item.isRead) .4f else 1f
    val surface = MaterialTheme.colorScheme.surfaceVariant
    val onSurface = MaterialTheme.colorScheme.onSurface

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 4‑dp colored bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = dimmedAlpha))
            )


            // text block
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.source,
                    style = MaterialTheme.typography.labelMedium,
                    color = onSurface.copy(alpha = 0.6f * dimmedAlpha)
                )

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = onSurface.copy(alpha = dimmedAlpha),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = formatDate(item.pubDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurface.copy(alpha = 0.5f * dimmedAlpha)
                )
            }

            // thumbnail

            Spacer(Modifier.width(12.dp))

            if (item.thumbnailUrl.isNotBlank()) {
                Spacer(Modifier.width(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(item.thumbnailUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }
}

fun formatDate(dateStr: String): String {
    return try {
        val parser = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
        val date = parser.parse(dateStr)
        val formatter = SimpleDateFormat("HH:mm dd MMM yyyy", Locale.ENGLISH)
        formatter.format(date!!)
    } catch (_: Exception) {
        dateStr
    }
}