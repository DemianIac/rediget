package com.diacono.rediget.reader.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diacono.rediget.R
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailFragment : Fragment() {

    val viewModel: PostListViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_post_detail, container, false)
        return rootView
    }

    companion object {
        fun newInstance() =
            PostDetailFragment()

        const val ARG_ITEM_ID = "item_id"
    }
}