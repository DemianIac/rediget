package com.diacono.rediget.reader.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Post(
    @SerializedName("subreddit") val subreddit: String,
    @SerializedName("title") val title: String,
    @SerializedName("name") val name: String,
    @SerializedName("author") val author: String,
    @SerializedName("thumbnail") val thumbnail: String?,
    @SerializedName("num_comments") val comments: Int,
    @SerializedName("created_utc") val created: Long
): Serializable
