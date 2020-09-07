package com.diacono.rediget.reader.presentation

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.diacono.rediget.R
import androidx.lifecycle.Observer
import com.diacono.rediget.reader.domain.model.Post
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostListActivity : AppCompatActivity() {

    val viewModel: PostListViewModel by viewModel()

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
                replace(R.id.activity_container, PostListFragment.newInstance()).addToBackStack(POST_LIST)
            }
        }
    }

    private fun observeProperty() {
        viewModel.postList.observe(this, Observer {
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
        recyclerView.adapter = PostRecyclerViewAdapter(
            posts,
            itemClickListener()
        )
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

    companion object{
        const val POST_LIST = "post_list"
    }
}