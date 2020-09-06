package com.diacono.rediget.reader.presentation

import android.os.Bundle
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import com.diacono.rediget.R

import com.diacono.rediget.dummy.DummyContent

class PostListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        setupRecyclerView(findViewById(R.id.item_list))
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = PostRecyclerViewAdapter(
            this,
            DummyContent.ITEMS,
            isInTwoPaneMode()
        )
    }

    private fun isInTwoPaneMode() =
        findViewById<NestedScrollView>(R.id.item_detail_container) != null
}