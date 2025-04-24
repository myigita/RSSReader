package com.example.rssreader

import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class FeedSource(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val url: String = "",
    val icon: String = "",
    val tags: List<FeedTag> = emptyList(),

)

@Serializable
data class FeedTag(
    val name: String = "",
)