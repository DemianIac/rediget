package com.diacono.rediget.reader.presentation.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import com.diacono.rediget.R
import com.diacono.rediget.reader.presentation.PostListActivity

class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        setSupportActionBar(findViewById(R.id.detail_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            val fragment = PostDetailFragment()
                .apply {
                arguments = Bundle().apply {
                    putString(
                        PostDetailFragment.ARG_ITEM_ID,
                            intent.getStringExtra(PostDetailFragment.ARG_ITEM_ID))
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.post_detail_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    navigateUpTo(Intent(this, PostListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}