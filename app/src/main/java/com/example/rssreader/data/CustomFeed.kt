package com.example.rssreader.data

import kotlinx.serialization.Serializable

@Serializable
data class CustomFeed (
    var title: String = "",
    var expression : String = "",
    var includedFeedSourceIds: List<String> = emptyList(),
)
