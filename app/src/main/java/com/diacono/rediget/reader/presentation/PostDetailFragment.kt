package com.diacono.rediget.reader.presentation

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.diacono.rediget.R
import com.diacono.rediget.commons.BaseFragment
import com.diacono.rediget.commons.ImageDownloader
import com.diacono.rediget.reader.domain.model.Post
import kotlinx.android.synthetic.main.item_post_detail.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.File
import java.net.URL


class PostDetailFragment : BaseFragment() {

    private val viewModel: PostListViewModel by sharedViewModel()

    override fun layout() = R.layout.item_post_detail

    override fun observeProperty() {
        viewModel.selectedPost.observe(viewLifecycleOwner, Observer {
            setupData(it)
        })
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showMessage(it)
        })
    }

    private fun setupData(post: Post?) {
        post?.let { post ->
            vNameDetail.text = post.author
            vTitleDetail.text = post.title
            vThumbnailDetail.setOnClickListener { onThumbnailDetailClicked(post.thumbnail) }
            loadThumbnail(post)
            vDownloadThumbnail.isVisible = true
        } ?: setEmptyDetail()
    }

    override fun setListeners() {
        vDownloadThumbnail.setOnClickListener {
            viewModel.selectedPost.value?.let {
                saveImage(it)
            }
        }
    }

    private fun saveImage(post: Post) {
        post.thumbnail?.let {
            ImageDownloader.saveImage(requireContext(), it, post.author)
            showMessage(resources.getString(R.string.downloading_image))
        }
    }

    private fun showMessage(string: String) {
        Toast.makeText(
            requireContext(),
            string,
            Toast.LENGTH_SHORT
        ).show()
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
        vDownloadThumbnail.isVisible = false
    }

    private fun loadThumbnail(it: Post) {
        val roundedCorners =
            RoundedCorners(this.resources.getDimensionPixelSize(R.dimen.thumbnail_corner_radious))
        Glide.with(requireContext())
            .load(it.thumbnail)
            .fitCenter()
            .transform(roundedCorners)
            .into(vThumbnailDetail)
    }

    companion object {
        const val VALID_URL = "https://"
        fun newInstance() = PostDetailFragment()
    }
}