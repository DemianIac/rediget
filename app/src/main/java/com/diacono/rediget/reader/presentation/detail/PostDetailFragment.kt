package com.diacono.rediget.reader.presentation.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.diacono.rediget.R
import com.diacono.rediget.dummy.DummyContent

class PostDetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_post_detail, container, false)
        return rootView
    }

    companion object {
        fun newInstance() = PostDetailFragment()

        const val ARG_ITEM_ID = "item_id"
    }
}