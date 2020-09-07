package com.diacono.rediget

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.diacono.rediget.reader.domain.core.actions.GetTopPosts
import com.diacono.rediget.reader.domain.model.Post
import com.diacono.rediget.reader.domain.model.exception.TopPostException
import com.diacono.rediget.reader.infraestructure.response.RedditChildrenResponse
import com.diacono.rediget.reader.infraestructure.response.RedditDataResponse
import com.diacono.rediget.reader.infraestructure.response.RedditPostResponse
import com.diacono.rediget.reader.infraestructure.response.RedditResponse
import com.diacono.rediget.reader.presentation.PostListViewModel
import com.diacono.rediget.utils.RxImmediateSchedulerRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class PostListViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val schedulers = RxImmediateSchedulerRule()

    private val getTopPostsAction: GetTopPosts = mock()
    private lateinit var postListViewModel: PostListViewModel

    @Test
    fun `getting a list of top posts should succeed`() {
        givenValidPostsResponseAmmount(POST_LIMIT, REDDIT_POST_RESPONSE)
        givenAViewModel()
        whenGettingTopPost(POST_LIMIT)
        thenPostAreReturned(POST_LIST)
    }

    @Test
    fun `getting a list of top posts should fail`() {
        givenPostsResponseAmountWithError(POST_LIMIT)
        givenAViewModel()
        thenErrorMessageIs(POST_ERROR_MESSAGE)
    }

    private fun givenPostsResponseAmountWithError(limit: Int) {
        whenever(getTopPostsAction(limit)).thenReturn(
            Single.just(
                Response.error<RedditResponse>(
                    POST_ACTION_ERROR_STATUS_CODE,
                    getResponseBodyWithMessage(TopPostException().message)
                )
            )
        )
    }

    private fun thenErrorMessageIs(message: String) {
        assertEquals(message, postListViewModel.errorMessage.value)
    }

    private fun getResponseErrorWithMessage(code: Int, message: String): Response<Void> {
        return Response.error<Void>(code, getResponseBodyWithMessage(message))
    }

    private fun getResponseBodyWithMessage(message: String) =
        message.toResponseBody("application/json; charset=utf-8".toMediaType())

    private fun thenPostAreReturned(response: List<Post>) {
        assertEquals(response, postListViewModel.postList.value)
    }

    private fun givenValidPostsResponseAmmount(limit: Int, postList: RedditResponse) {
        whenever(getTopPostsAction(limit)).thenReturn(
            Single.just(Response.success<RedditResponse>(postList))
        )
    }

    private fun whenGettingTopPost(limit: Int) {
      postListViewModel.getTopPost(limit)
    }

    private fun givenAViewModel() {
        postListViewModel = PostListViewModel(getTopPostsAction)
    }

    companion object {
        const val POST_LIMIT = 10
        const val POST_ACTION_ERROR_STATUS_CODE = 404
        val POST_ERROR_MESSAGE = TopPostException().message

        private val redditPostResponse =
            RedditPostResponse(
                "superhero",
                "Test",
                "Post",
                "Demian",
                null,
                0,
                123L
            )
        val REDDIT_POST_RESPONSE =
            RedditResponse(RedditDataResponse(listOf(RedditChildrenResponse(redditPostResponse))))

        val POST_LIST = listOf(
            Post(
                "superhero",
                "Test",
                "Post",
                "Demian",
                null,
                0,
                123L
            )
        )
    }

}