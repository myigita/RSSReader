package com.example.rssreader.data

data class FeedItem(
    var title: String = "",
    var content: String = "",
    var link: String = "",
    var pubDate: String = "",
    var thumbnailUrl: String = "",
    var source: String = "",
    var isRead: Boolean = false,
) {
    override fun toString(): String {
        return title
    }
}