package com.diacono.rediget

import com.diacono.rediget.reader.domain.core.actions.GetTopPosts
import com.diacono.rediget.reader.infraestructure.response.RedditResponse
import com.diacono.rediget.reader.domain.services.PostService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Single
import org.junit.Test
import retrofit2.Response

class GetTopPostsActionTest {
    private lateinit var postList: Single<Response<RedditResponse>>
    private var postService: PostService = mock()
    private lateinit var action: GetTopPosts

    @Test
    fun `getting top posts with limit should succeed`() {
        givenAction()
        whenTheTopPostAreReturned(POST_LIMIT)
        thenRequestOfGetTopIsMade(POST_LIMIT)
    }

    private fun thenRequestOfGetTopIsMade(limit: Int) {
        verify(postService).getTop(limit)
    }

    private fun givenAction() {
        this.action = GetTopPosts(postService)
    }

    private fun whenTheTopPostAreReturned(limit: Int) {
        postList = action(limit)
    }

    companion object {
        const val POST_LIMIT = 10
    }
}

