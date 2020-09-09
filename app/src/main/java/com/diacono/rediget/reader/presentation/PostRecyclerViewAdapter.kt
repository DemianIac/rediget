package com.diacono.rediget.reader.presentation

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diacono.rediget.R
import com.diacono.rediget.reader.domain.model.Post
import java.util.concurrent.TimeUnit

typealias onPostClicked = (Post) -> Unit
typealias onDismissPostClicked = (Post) -> Unit

class PostRecyclerViewAdapter(
    private val onPostClicked: onPostClicked,
    private val onDismissPostClicked: onDismissPostClicked
) : ListAdapter<Post, PostListViewHolder>(
    object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.created == newItem.created
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostListViewHolder(parent)

    override fun onBindViewHolder(holder: PostListViewHolder, position: Int) {
        holder.populate(getItem(position), onPostClicked, onDismissPostClicked)
    }
}

class PostListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_post_list, parent, false)
) {
    private val name: TextView = itemView.findViewById(R.id.vName)
    private val creation: TextView = itemView.findViewById(R.id.vCreation)
    private val title: TextView = itemView.findViewById(R.id.vTitle)
    private val thumbnail: ImageView = itemView.findViewById(R.id.vThumbnail)
    private val dismiss: Button = itemView.findViewById(R.id.vDismiss)
    private val comments: TextView = itemView.findViewById(R.id.vComments)
    private val itemFrame: View = itemView.findViewById(R.id.vItemFrame)

    fun populate(
        item: Post,
        onPostClicked: onPostClicked,
        onDismissPostClicked: onDismissPostClicked
    ) {
        name.text = item.author
        creation.text = getTimesInHours(item)
        title.text = item.title
        loadThumbnail(item)
        comments.text = getCommentsAmount(item)
        itemFrame.setOnClickListener { onPostClicked(item) }
        dismiss.setOnClickListener { onDismissPostClicked(item) }
        itemView.tag = item
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

    private fun loadThumbnail(
        item: Post
    ) = Glide.with(itemView.context).load(item.thumbnail)
        .fitCenter()
        .into(thumbnail)
}