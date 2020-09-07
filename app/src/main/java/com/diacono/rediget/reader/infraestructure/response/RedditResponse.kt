package com.diacono.rediget.reader.infraestructure.response

import com.google.gson.annotations.SerializedName

data class RedditResponse(
    @SerializedName("data")
    val data: RedditDataResponse
)

data class RedditDataResponse(
    @SerializedName("children")
    val children: List<RedditChildrenResponse>
)

data class RedditChildrenResponse(
    @SerializedName("data")
    val data: RedditPostResponse
)

data class RedditPostResponse(
    @SerializedName("subreddit") val subreddit: String,
    @SerializedName("title") val title: String,
    @SerializedName("name") val name: String,
    @SerializedName("author") val author: String,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("num_comments") val comments: Int,
    @SerializedName("created_utc") val created: Long
)