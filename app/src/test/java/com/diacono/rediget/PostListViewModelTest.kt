package com.diacono.rediget

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.diacono.rediget.reader.domain.core.actions.GetMoreTopPosts
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
import org.junit.Assert.assertTrue
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
    private val getMoreTopPostsAction: GetMoreTopPosts = mock()
    private val savedStateHandle: SavedStateHandle = mock()
    private lateinit var postListViewModel: PostListViewModel

    @Test
    fun `getting a list of top posts should succeed`() {
        givenValidPostsResponseAmount(POST_LIMIT, REDDIT_POST_RESPONSE)
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

    @Test
    fun `adding a selected post should update de live data`() {
        givenValidPostsResponseAmount(POST_LIMIT, REDDIT_POST_RESPONSE)
        givenAViewModel()
        whenSelectingAPost(POST_LIST.first())
        thenPostIsSelected(POST_LIST.first())
    }

    @Test
    fun `with a selected post stored it should be restored when creating the view model`() {
        givenValidPostsResponseAmount(POST_LIMIT, REDDIT_POST_RESPONSE)
        givenASelectedPostStored(POST_LIST.first())
        givenAViewModel()
        thenPostIsRestored(POST_LIST.first())
    }

    @Test
    fun `loading more data from server should succeed`() {
        givenValidPostsResponseAmount(POST_LIMIT, REDDIT_POST_RESPONSE)
        givenAValidResponseWithMorePosts(POST_LIMIT, POST_AFTER, REDDIT_POST_RESPONSE)
        givenAViewModel()
        whenMorePostAreLoaded(POST_LIMIT, POST_AFTER)
        thenMorePostAreReturned(POST_LIST)
    }

    @Test
    fun `refreshing top post should reset the list of posts`() {
        givenValidPostsResponseAmount(POST_LIMIT, REDDIT_POST_RESPONSE)
        givenAViewModel()
        givenValidPostsResponseAmount(POST_LIMIT, REDDIT_REFRESH_POST_RESPONSE)
        whenRefreshIsDone()
        thenPostAreReturned(POST_REFRESH_LIST)
    }

    @Test
    fun `dismissing a post should remove it from the list of posts`() {
        givenValidPostsResponseAmount(POST_LIMIT, REDDIT_DISMISS_RESPONSE)
        givenAViewModel()
        givenASelectedPost(POST_REFRESH_LIST.first())
        whenDismissingAPost(POST_REFRESH_VIEWED_LIST.first())
        thenPostAreReturned(POST_LIST)
        thenSelectedDismissPostIsCleared()
    }

    @Test
    fun `dismissing all post should them the list of posts`() {
        givenValidPostsResponseAmount(POST_LIMIT, REDDIT_POST_RESPONSE)
        givenAViewModel()
        givenASelectedPost(POST_REFRESH_LIST.first())
        whenDismissAllPosts()
        thenAllPostsAreDeleted()
        thenSelectedDismissPostIsCleared()
    }

    @Test
    fun `selecting a post should persist the reading status`() {
        givenValidPostsResponseAmount(POST_LIMIT, REDDIT_POST_RESPONSE)
        givenAViewModel()
        whenSelectingAPost(POST_LIST.first())
        thenReadPostIsPersisted(POST_LIST.first().name)
    }

    @Test
    fun `retrieving data should match de unread status before updating the list of posts`() {
        givenValidPostsResponseAmount(POST_LIMIT, REDDIT_POST_RESPONSE)
        givenViewedList(POST_LIST.first().name)
        givenAViewModel()
        whenGettingTopPost(POST_LIMIT)
        thenPostListHasUpdatedUnreadStatus(POST_LIST.first().name)
    }

    private fun thenPostListHasUpdatedUnreadStatus(name: String) {
        assertTrue(postListViewModel.postList.value?.first{it.name == name}?.unread == false)
    }

    private fun givenViewedList(name: String) {
        whenever(savedStateHandle.get<ArrayList<String>>(PostListViewModel.VIEWED_POSTS)).thenReturn(arrayListOf(name))
    }

    private fun thenReadPostIsPersisted(name: String) {
        assertTrue(postListViewModel.viewedPosts.contains(name))
    }

    private fun givenASelectedPost(post: Post) {
        postListViewModel.onSelectedPost(post)
    }

    private fun thenAllPostsAreDeleted() {
        postListViewModel.postList.value?.isEmpty()?.let { assertTrue(it) }
    }

    private fun whenDismissAllPosts() {
        postListViewModel.onDismissAllPosts()
    }

    private fun thenSelectedDismissPostIsCleared() {
        assertTrue(postListViewModel.selectedPost.value == null)
    }

    private fun whenDismissingAPost(post: Post) {
        postListViewModel.onDismissPost(post)
    }

    private fun whenRefreshIsDone() {
        postListViewModel.refreshPosts()
    }

    private fun thenMorePostAreReturned(postList: List<Post>) {
        assertEquals(postList + postList, postListViewModel.postList.value)
    }

    private fun whenMorePostAreLoaded(limit: Int, after: String) {
        postListViewModel.loadMorePosts(limit, after)
    }

    private fun givenAValidResponseWithMorePosts(
        limit: Int,
        after: String,
        postList: RedditResponse
    ) {
        whenever(getMoreTopPostsAction(limit, after)).thenReturn(
            Single.just(Response.success<RedditResponse>(postList))
        )
    }

    private fun thenPostIsRestored(post: Post) {
        assertEquals(post, postListViewModel.selectedPost.value)
    }

    private fun givenASelectedPostStored(post: Post) {
        whenever(savedStateHandle.get<Post>(PostListViewModel.SELECTED_POST)).thenReturn(post)
    }

    private fun thenPostIsSelected(selectedPost: Post) {
        assertEquals(selectedPost, postListViewModel.selectedPost.value)
    }

    private fun whenSelectingAPost(selectedPost: Post) {
        postListViewModel.onSelectedPost(selectedPost)
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

    private fun givenValidPostsResponseAmount(limit: Int, postList: RedditResponse) {
        whenever(getTopPostsAction(limit)).thenReturn(
            Single.just(Response.success<RedditResponse>(postList))
        )
    }

    private fun whenGettingTopPost(limit: Int) {
        postListViewModel.getTopPost(limit)
    }

    private fun givenAViewModel() {
        postListViewModel =
            PostListViewModel(savedStateHandle, getTopPostsAction, getMoreTopPostsAction)
    }

    companion object {
        const val POST_LIMIT = 10
        const val POST_AFTER = "Test"
        const val POST_ACTION_ERROR_STATUS_CODE = 404
        val POST_ERROR_MESSAGE = TopPostException().message

        private val redditPostResponse =
            RedditPostResponse(
                "superhero",
                "Test",
                "Post list",
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
                "Post list",
                "Demian",
                null,
                0,
                123L
            )
        )

        private val redditRefreshPostResponse =
            RedditPostResponse(
                "superhero",
                "Test",
                "Post refresh",
                "Carl",
                null,
                0,
                123L
            )

        val REDDIT_REFRESH_POST_RESPONSE =
            RedditResponse(
                RedditDataResponse(
                    listOf(
                        RedditChildrenResponse(
                            redditRefreshPostResponse
                        )
                    )
                )
            )

        val POST_REFRESH_LIST = listOf(
            Post(
                "superhero",
                "Test",
                "Post refresh",
                "Carl",
                null,
                0,
                123L
            )
        )

        val POST_REFRESH_VIEWED_LIST = listOf(
            Post(
                "superhero",
                "Test",
                "Post refresh",
                "Carl",
                null,
                0,
                123L,
                false
            )
        )

        val REDDIT_DISMISS_RESPONSE =
            RedditResponse(
                RedditDataResponse(
                    listOf(
                        RedditChildrenResponse(redditRefreshPostResponse), RedditChildrenResponse(
                            redditPostResponse
                        )
                    )
                )
            )

    }

}