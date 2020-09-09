package com.diacono.rediget.reader.presentation

import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.ViewTarget
import com.diacono.rediget.R
import com.diacono.rediget.reader.domain.model.Post
import kotlinx.android.synthetic.main.item_post_list.view.*
import java.util.concurrent.TimeUnit


typealias onPostClicked = (Post) -> Unit
typealias onDismissPostClicked = (Post) -> Unit
typealias onThumbnailPostClicked = (String?) -> Unit

class PostRecyclerViewAdapter(
    private val onPostClicked: onPostClicked,
    private val onDismissPostClicked: onDismissPostClicked,
    private val onThumbnailPostClicked: onThumbnailPostClicked
) : ListAdapter<Post, PostListViewHolder>(
    object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.created == newItem.created &&
                    oldItem.unread == newItem.unread
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostListViewHolder(parent)

    override fun onBindViewHolder(holder: PostListViewHolder, position: Int) {
        holder.populate(
            getItem(position),
            onPostClicked,
            onDismissPostClicked,
            onThumbnailPostClicked
        )
    }

    override fun submitList(@Nullable list: List<Post>?) {
        super.submitList(list?.apply { if (itemCount == list.size) notifyDataSetChanged() })
    }
}

class PostListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_post_list, parent, false)
) {

    fun populate(
        item: Post,
        onPostClicked: onPostClicked,
        onDismissPostClicked: onDismissPostClicked,
        onThumbnailPostClicked: onThumbnailPostClicked
    ) {
        with(itemView) {
            vName.text = resources.getString(R.string.by_author, item.author)
            vCreation.text = getTimesInHours(item)
            vTitle.text = item.title
            loadThumbnail(item)
            vThumbnail.setOnClickListener { onThumbnailPostClicked(item.thumbnail) }
            vComments.text = getCommentsAmount(item)
            vItemFrame.setOnClickListener { onPostClicked(item) }
            vDismiss.setOnClickListener { onDismissPostClicked(item) }
            vUnreadNotification.isVisible = item.unread
            tag = item
        }
    }

    private fun getCommentsAmount(
        item: Post
    ) = itemView.context.getString(R.string.comments).format(item.comments)

    private fun getTimesInHours(item: Post): CharSequence? {
        return DateUtils.getRelativeTimeSpanString(
            TimeUnit.SECONDS.toMillis(item.created),
            System.currentTimeMillis(),
            DateUtils.HOUR_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        )
    }

    private fun loadThumbnail(item: Post) {
        val roundedCorners =
            RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.thumbnail_corner_radious))
        Glide.with(itemView.context)
            .load(item.thumbnail)
            .fitCenter()
            .transform(roundedCorners)
            .into(itemView.vThumbnail)
    }
}