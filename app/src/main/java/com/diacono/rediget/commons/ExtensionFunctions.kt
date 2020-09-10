package com.diacono.rediget.commons

import androidx.lifecycle.MutableLiveData
import com.diacono.rediget.reader.domain.model.Post
import com.diacono.rediget.reader.infraestructure.response.RedditPostResponse

fun MutableLiveData<List<Post>>.changeUnreadStatus(post: Post, unread: Boolean) {
    val value = this.value ?: emptyList()
    value.find { it.name == post.name }?.unread = unread
    this.value = value
}

fun <T> MutableLiveData<List<T>>.plusAssign(items: List<T>) {
    val value = this.value ?: emptyList()
    this.value = value + items
}

fun MutableLiveData<List<Post>>.minusAssign(item: Post) {
    val value = this.value ?: emptyList()
    this.value = value.minus(item)
}

fun RedditPostResponse.toPost() = Post(
    this.subreddit,
    this.title,
    this.name,
    this.author,
    this.thumbnail,
    this.comments,
    this.created
)