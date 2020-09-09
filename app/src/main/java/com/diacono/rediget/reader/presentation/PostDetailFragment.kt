package com.diacono.rediget.reader.presentation

import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.diacono.rediget.R
import com.diacono.rediget.commons.BaseFragment
import com.diacono.rediget.reader.domain.model.Post
import kotlinx.android.synthetic.main.item_post_detail.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PostDetailFragment : BaseFragment() {

    private val viewModel: PostListViewModel by sharedViewModel()

    override fun layout() = R.layout.item_post_detail

    override fun observeProperty() {
        viewModel.selectedPost.observe(viewLifecycleOwner, Observer {
            setupData(it)
        })
    }

    private fun setupData(post: Post?) {
        post?.let {
            vNameDetail.text = it.name
            vTitleDetail.text = it.title
            loadThumbnail(it)
        }?: setEmptyDetail()
    }

    private fun setEmptyDetail() {
        vNameDetail.text = ""
        vTitleDetail.text = ""
        vThumbnailDetail.setImageResource(0)
    }

    private fun loadThumbnail(it: Post) =
        Glide.with(requireContext()).load(it.thumbnail)
            .fitCenter()
            .into(vThumbnailDetail)

    companion object {
        fun newInstance() =
            PostDetailFragment()
    }
}