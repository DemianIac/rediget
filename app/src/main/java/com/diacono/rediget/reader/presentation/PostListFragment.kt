package com.diacono.rediget.reader.presentation


import com.diacono.rediget.R
import com.diacono.rediget.commons.BaseFragment
import kotlinx.android.synthetic.main.fragment_post_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.diacono.rediget.reader.domain.model.Post


class PostListFragment : BaseFragment() {

    private val viewModel: PostListViewModel by sharedViewModel()
    private var mLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
    private lateinit var postAdapter: PostRecyclerViewAdapter
    private var loading = true

    override fun layout() = R.layout.fragment_post_list

    override fun init() {
        setupRecyclerView()
    }

    override fun setListeners() {
        vDismissAll.setOnClickListener { onDismissAllPosts()}
    }

    override fun observeProperty() {
        viewModel.postList.observe(this, Observer {
            loading = true
            vSwipeToRefresh.isRefreshing = false
            postAdapter.submitList(it)
        })
    }

    private fun setupRecyclerView() {
        //Todo PagedList implementation would be better here
        vPostListRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                loadMoreIfNeeded(dy)
            }
        })
        vPostListRecycler.layoutManager = mLayoutManager
        postAdapter = PostRecyclerViewAdapter(
            this@PostListFragment::onPostClicked,
            this@PostListFragment::onDismissPostClicked
        )
        vPostListRecycler.adapter = postAdapter
        vSwipeToRefresh.setOnRefreshListener(
            SwipeRefreshLayout.OnRefreshListener { viewModel.refreshPosts() }
        )
    }

    private fun loadMoreIfNeeded(dy: Int) {
        if (needsToLoadMorePosts(dy)) {
            loading = false
            viewModel.onNeedToLoadMorePosts()
        }
    }

    private fun needsToLoadMorePosts(deltaY: Int) = deltaY > 0 && loading && nearToLastVisible()

    private fun nearToLastVisible(): Boolean {
        with(mLayoutManager) { return childCount + findFirstVisibleItemPosition() >= itemCount }
    }

    private fun onDismissAllPosts() {
        viewModel.onDismissAllPosts()
    }

    private fun onPostClicked(post: Post) {
        viewModel.onSelectedPost(post)
    }

    private fun onDismissPostClicked(post: Post) {
        viewModel.onDismissPost(post)
    }

    companion object {
        fun newInstance() = PostListFragment()
    }
}