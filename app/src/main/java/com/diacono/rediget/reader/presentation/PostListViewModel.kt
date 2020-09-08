package com.diacono.rediget.reader.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.diacono.rediget.reader.domain.core.actions.GetMoreTopPosts
import com.diacono.rediget.reader.domain.core.actions.GetTopPosts
import com.diacono.rediget.reader.domain.model.Post
import com.diacono.rediget.reader.infraestructure.response.RedditPostResponse
import com.diacono.rediget.reader.infraestructure.response.RedditResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class PostListViewModel(
    val savedStateHandle: SavedStateHandle,
    val topPostsAction: GetTopPosts,
    val moreTopPostsAction: GetMoreTopPosts
) : ViewModel() {

    private var mutablePostList = MutableLiveData<List<Post>>()
    val postList: LiveData<List<Post>> = mutablePostList
    private var mutableErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = mutableErrorMessage
    private var mutableSelectedPost = MutableLiveData<Post>()
    val selectedPost: LiveData<Post> = mutableSelectedPost

    init {
        getTopPost(PAGINATION_SIZE)
        savedStateHandle.get<Post>(SELECTED_POST)?.let {
            onSelectedPost(it)
        }
    }

    @SuppressLint("CheckResult")
    fun getTopPost(limit: Int) {
        topPostsAction(limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setActionState(it)
            }, { t ->
                mutableErrorMessage.value = t!!.message
            })
    }

    private fun setActionState(response: Response<RedditResponse>) {
        if (response.isSuccessful)
            requireNotNull(response.body()).let { mutablePostList.value = createPostList(it) }
        else
            mutableErrorMessage.value = response.errorBody()?.string()
    }

    private fun createPostList(redditResponse: RedditResponse): List<Post> {
        return redditResponse.data.children.map { it.data.toPost() }
    }

    fun onSelectedPost(selectedPost: Post) {
        mutableSelectedPost.value = selectedPost
        savedStateHandle.set(SELECTED_POST, selectedPost)
    }

    @SuppressLint("CheckResult")
    fun loadMorePosts(limit: Int, after: String) {
        moreTopPostsAction(limit, after)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setMorePostsActionState(it)
            }, { t ->
                mutableErrorMessage.value = t!!.message
            })
    }

    private fun setMorePostsActionState(response: Response<RedditResponse>) {
        if (response.isSuccessful)
            requireNotNull(response.body()).let { mutablePostList.value =
                mutablePostList.value?.plus(createPostList(it)) }
        else
            mutableErrorMessage.value = response.errorBody()?.string()
    }

    fun onNeedToLoadMorePosts() {
        mutablePostList.value?.last()?.name?.let { loadMorePosts(PAGINATION_SIZE, it) }
    }

    fun refreshPosts() {
        getTopPost(PAGINATION_SIZE)
    }

    companion object {
        const val PAGINATION_SIZE = 10
        const val SELECTED_POST = "SELECTED_POST"
    }

}

private fun RedditPostResponse.toPost() = Post(
    this.subreddit,
    this.title,
    this.name,
    this.author,
    this.thumbnail,
    this.comments,
    this.created
)
