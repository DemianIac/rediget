package com.diacono.rediget.reader.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ViewTarget
import com.diacono.rediget.R
import com.diacono.rediget.commons.BaseFragment
import com.diacono.rediget.reader.domain.model.Post
import kotlinx.android.synthetic.main.item_post_detail.*
import kotlinx.android.synthetic.main.item_post_list_content.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailFragment : BaseFragment() {

    val viewModel: PostListViewModel by sharedViewModel()

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
        }
    }

    private fun loadThumbnail(it: Post) =
        Glide.with(requireContext()).load(it.thumbnail)
            .fitCenter()
            .into(vThumbnailDetail)

    companion object {
        fun newInstance() =
            PostDetailFragment()

        const val ARG_ITEM_ID = "item_id"
    }
}