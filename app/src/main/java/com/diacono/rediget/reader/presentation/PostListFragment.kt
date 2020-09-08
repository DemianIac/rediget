package com.diacono.rediget.reader.presentation

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.diacono.rediget.R
import com.diacono.rediget.commons.BaseFragment
import kotlinx.android.synthetic.main.fragment_post_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class PostListFragment : BaseFragment() {

    val viewModel: PostListViewModel by sharedViewModel()

    override fun layout() = R.layout.fragment_post_list

    companion object {
        fun newInstance() = PostListFragment()
    }
}