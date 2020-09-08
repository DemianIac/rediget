package com.diacono.rediget

import com.diacono.rediget.reader.domain.core.actions.GetMoreTopPosts
import com.diacono.rediget.reader.domain.core.actions.GetTopPosts
import com.diacono.rediget.reader.infraestructure.response.RedditResponse
import com.diacono.rediget.reader.domain.services.PostService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Single
import org.junit.Test
import retrofit2.Response

class GetMoreTopPostsActionTest {
    private lateinit var postList: Single<Response<RedditResponse>>
    private var postService: PostService = mock()
    private lateinit var action: GetMoreTopPosts

    @Test
    fun `getting top posts with limit should succeed`() {
        givenAction()
        whenMoreTopPostAreReturned(POST_LIMIT, POST_AFTER)
        thenRequestOfGetTopIsMade(POST_LIMIT, POST_AFTER)
    }

    private fun thenRequestOfGetTopIsMade(limit: Int, after: String) {
        verify(postService).getMoreTop(limit, after)
    }

    private fun givenAction() {
        this.action = GetMoreTopPosts(postService)
    }

    private fun whenMoreTopPostAreReturned(limit: Int, after: String) {
        postList = action(limit, after)
    }

    companion object {
        const val POST_LIMIT = 10
        const val POST_AFTER = "test"
    }
}

