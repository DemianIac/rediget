package com.diacono.rediget.reader.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diacono.rediget.reader.domain.core.actions.GetTopPosts
import com.diacono.rediget.reader.domain.model.Post
import com.diacono.rediget.reader.infraestructure.response.RedditChildrenResponse
import com.diacono.rediget.reader.infraestructure.response.RedditPostResponse
import com.diacono.rediget.reader.infraestructure.response.RedditResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class PostListViewModel(val topPostsAction: GetTopPosts) : ViewModel() {

    private var mutablePostList = MutableLiveData<List<Post>>()
    val postList: LiveData<List<Post>> = mutablePostList
    private var mutableErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = mutableErrorMessage

    init {
        getTopPost(PAGINATION_ZIE)
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

    companion object {
        const val PAGINATION_ZIE = 10
    }

}

private fun RedditPostResponse.toPost() = Post(this.subreddit,this.title,this.name,this.author,this.thumbnail,this.comments,this.created)
