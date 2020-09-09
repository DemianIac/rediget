package com.diacono.rediget.reader.presentation

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.diacono.rediget.R
import com.diacono.rediget.commons.BaseFragment
import com.diacono.rediget.reader.domain.model.Post
import kotlinx.android.synthetic.main.item_post_detail.*
import kotlinx.android.synthetic.main.item_post_list.*
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
        post?.let { post ->
            vNameDetail.text = post.name
            vTitleDetail.text = post.title
            vThumbnailDetail.setOnClickListener { onThumbnailDetailClicked(post.thumbnail) }
            loadThumbnail(post)
        } ?: setEmptyDetail()
    }

    private fun onThumbnailDetailClicked(thumbnail: String?) {
        thumbnail?.let { openBrowser(thumbnail) }
    }

    private fun openBrowser(thumbnail: String) {
        if (thumbnail.startsWith(VALID_URL)) {
            val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(thumbnail) }
            startActivity(intent)
        }
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
        const val VALID_URL = "https://"
        fun newInstance() = PostDetailFragment()
    }
}