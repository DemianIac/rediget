package com.diacono.rediget.dummys

import com.diacono.rediget.reader.domain.model.Post
import com.diacono.rediget.reader.infraestructure.response.RedditPostResponse

object PostListViewModelTestFactory {

    fun createDummyRedditPostRefreshResponse(): RedditPostResponse {
        return RedditPostResponse(
            "superhero",
            "Test",
            "Post refresh",
            "Carl",
            null,
            0,
            123L
        )
    }

    fun createDummyPostRefreshViewed(): Post {
        return Post(
            "superhero",
            "Test",
            "Post refresh",
            "Carl",
            null,
            0,
            123L,
            false
        )
    }

    fun createDummyPostRefresh(): Post {
        return Post(
            "superhero",
            "Test",
            "Post refresh",
            "Carl",
            null,
            0,
            123L
        )
    }

    fun createDummyPostList(): Post {
        return Post(
            "superhero",
            "Test",
            "Post list",
            "Demian",
            null,
            0,
            123L
        )
    }

    fun createDummyRedditListResponse(): RedditPostResponse {
        return RedditPostResponse(
            "superhero",
            "Test",
            "Post list",
            "Demian",
            null,
            0,
            123L
        )
    }
}

