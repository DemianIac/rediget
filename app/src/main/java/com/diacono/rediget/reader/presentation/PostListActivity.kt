package com.diacono.rediget.reader.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import com.diacono.rediget.R
import androidx.lifecycle.Observer
import com.diacono.rediget.reader.domain.model.Post
import com.diacono.rediget.reader.presentation.detail.PostDetailActivity
import com.diacono.rediget.reader.presentation.detail.PostDetailFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostListActivity : AppCompatActivity() {

    val viewModel: PostListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)
        observeProperty()
        setupToolbar()
    }

    private fun observeProperty() {
        viewModel.postList.observe(this, Observer {
            setupRecyclerView(findViewById(R.id.item_list),it)
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
            this,
            posts,
            itemClickListener()
        )
    }

    private fun itemClickListener() = View.OnClickListener { v ->
        val item = v.tag as Post
        if (isInTwoPaneMode()) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.post_detail_container, PostDetailFragment.newInstance())
                .commit()
        } else {
            val intent = Intent(v.context, PostDetailActivity::class.java)
            v.context.startActivity(intent)
        }
    }

    private fun isInTwoPaneMode() =
        findViewById<NestedScrollView>(R.id.post_detail_container) != null
}