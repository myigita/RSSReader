package com.example.rssreader.data

import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
data class FeedSource(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var url: String = "",
    val icon: String = "",
    val tags: List<FeedTag> = emptyList(),

    )

@Serializable
data class FeedTag(
    val name: String = "",
)

// This function checks if the expression matches the tags of the FeedSource.
// Used for filtering sources based on the expression for Feeds.
fun FeedSource.matchesExpression(customFeed: CustomFeed): Boolean {
    val tagsSet = tags.map { it.name.lowercase() }.toSet()
    val isIncludedManually = customFeed.includedFeedSourceIds.contains(id)
    return ExpressionEvaluator.evaluate(customFeed.expression, tagsSet) || isIncludedManually
}