package com.example.rssreader


import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.rssreader.FeedSource

// Example RSS feed URL
val xkcdSource = FeedSource(name = "XKCD", url = "https://xkcd.com/rss.xml")
val nytSource = FeedSource(name = "NYT Top", url = "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml")
val bbcSource = FeedSource(name = "BBC", url = "https://feeds.bbci.co.uk/news/rss.xml")

// www.xkcd.com/rss.xml
suspend fun fetchRss(source: FeedSource = nytSource): List<FeedItem>
        = withContext(Dispatchers.IO) {
    val client = OkHttpClient.Builder().build()
    val request = Request.Builder().url(source.url).build()

    try {
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            Log.e("FeedParser", "Error fetching RSS feed: ${response.code}")
            return@withContext emptyList<FeedItem>()
        }

        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(response.body?.charStream())

        val result = mutableListOf<FeedItem>()
        var currentItem: FeedItem? = null
        var feedSourceTitle: String = source.name
        var eventType = parser.eventType

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> when (parser.name) {
                    "item" -> currentItem = FeedItem()
                    "title" -> {
                        if (currentItem == null && feedSourceTitle == source.name) {
                            // Grab feed-level title if available
                            feedSourceTitle = parser.nextText()
                        } else {
                            currentItem?.title = parser.nextText()
                        }
                    }

                    "link" -> currentItem?.link = parser.nextText()
                    "guid" -> if (currentItem?.link.isNullOrBlank()) {
                        currentItem?.link = parser.nextText()
                    }
                    "pubDate" -> currentItem?.pubDate = parser.nextText()
                    "media:thumbnail", "enclosure", "media:content" -> {
                        val url = extractUrlAttribute(parser)
                        val type = parser.getAttributeValue(null, "type")
                        val isImage = type?.startsWith("image") == true || url?.endsWith(".jpg") == true

                        if (url?.startsWith("http") == true && isImage) {
                            currentItem?.thumbnailUrl = url
                        }
                    }
                    "description" -> {
                        val desc = parser.nextText()
                        val thumbnail = extractImageFromDescription(desc)
                        if (!thumbnail.isNullOrBlank()) {
                            currentItem?.thumbnailUrl = thumbnail
                        }
                    }                }

                XmlPullParser.END_TAG -> if (parser.name == "item") {
                    // Use source name if no source title is found
                    if (currentItem?.source == null || currentItem.source == "") {
                        currentItem?.source = feedSourceTitle
                    }
                    currentItem?.let { result += it }
                    currentItem = null
                }
            }
            eventType = parser.next()
        }
        Log.d("FeedParser", "Parsed ${result.size} items from ${source.name}")
        result
    } catch (e: Exception) {
        Log.e("FeedParser", "Error parsing RSS feed: ${e.message}")
        // Toast
        Toast.makeText(null, "Error parsing RSS feed: ${e.message}", Toast.LENGTH_SHORT).show()
        emptyList<FeedItem>()
    }
}

// This function is used to extract the URL attribute from the XML parser
private fun extractUrlAttribute(parser: XmlPullParser): String? {
    for (i in 0 until parser.attributeCount) {
        if (parser.getAttributeName(i) == "url") {
            return parser.getAttributeValue(i)
        }
    }
    return null
}

// This function is used to extract the image URL from the description
private fun extractImageFromDescription(description: String): String? {
    val regex = Regex("<img.*?src=\"(.*?)\".*?>")
    val matchResult = regex.find(description)
    return matchResult?.groups?.get(1)?.value
}