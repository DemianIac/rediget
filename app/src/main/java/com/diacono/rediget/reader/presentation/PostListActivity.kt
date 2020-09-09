package com.diacono.rediget.reader.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.diacono.rediget.R
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.stateViewModel

class PostListActivity : AppCompatActivity() {

    private val viewModel: PostListViewModel by lifecycleScope.stateViewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)
        setupView()
        observeProperty()
        setupToolbar()
    }

    private fun observeProperty() {
        viewModel.selectedPost.observe(this, Observer {
            if (!isInTwoPaneMode() && it != null)
                moveToSingleDetail()
        })
    }

    private fun setupView() {
        if (isInTwoPaneMode()) {
            supportFragmentManager.inTransaction {
                replace(R.id.post_detail_container, PostDetailFragment.newInstance())
            }
        }
        supportFragmentManager.inTransaction {
            replace(R.id.activity_container, PostListFragment.newInstance())
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
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