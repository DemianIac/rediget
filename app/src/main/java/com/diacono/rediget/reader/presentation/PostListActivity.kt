package com.diacono.rediget.reader.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diacono.rediget.R
import com.diacono.rediget.reader.domain.model.Post
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.stateViewModel

class PostListActivity : AppCompatActivity() {

    private val viewModel: PostListViewModel by lifecycleScope.stateViewModel(this)
    private var mLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
    private var loading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)
        setupView()
        observeProperty()
        setupToolbar()
    }

    private fun setupView() {
        if (isInTwoPaneMode()) {
            supportFragmentManager.inTransaction {
                replace(R.id.post_detail_container, PostDetailFragment.newInstance())
            }
        } else {
            supportFragmentManager.inTransaction {
                replace(R.id.activity_container, PostListFragment.newInstance()).addToBackStack(
                    POST_LIST
                )
            }
        }
    }

    private fun observeProperty() {
        viewModel.postList.observe(this, Observer {
            loading = true
            setupRecyclerView(findViewById(R.id.item_list), it)
        })
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        posts: List<Post>
    ) {
        //Todo PagedList implementation would be better here
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    if (loading) {
                        if (needToLoadMorePosts()) {
                            loading = false
                            viewModel.onNeedToLoadMorePosts()
                        }
                    }
                }
            }
        })
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = PostRecyclerViewAdapter(
            posts,
            itemClickListener()
        )
    }

    private fun needToLoadMorePosts(): Boolean {
        with(mLayoutManager) { return childCount + findFirstVisibleItemPosition() >= itemCount }
    }

    private fun itemClickListener() = View.OnClickListener { v ->
        val item = v.tag as Post
        viewModel.onSelectedPost(item)
        if (!isInTwoPaneMode())
            moveToSingleDetail()
    }

    private fun moveToSingleDetail() {
        supportFragmentManager.inTransaction {
            add(R.id.activity_container, PostDetailFragment.newInstance()).addToBackStack(null)
        }
    }

    private fun isInTwoPaneMode() =
        findViewById<NestedScrollView>(R.id.post_detail_container) != null

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    companion object {
        const val POST_LIST = "post_list"
    }
}