package com.diacono.rediget.reader.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.diacono.rediget.R
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PostListFragment : Fragment() {

    val viewModel: PostListViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_post_list, container, false)
        return rootView
    }

    companion object {
        fun newInstance() = PostListFragment()

        const val ARG_ITEM_ID = "item_id"
    }
}