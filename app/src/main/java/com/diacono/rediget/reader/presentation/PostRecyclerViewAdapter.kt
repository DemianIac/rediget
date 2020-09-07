package com.diacono.rediget.reader.presentation

import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.ViewTarget
import com.diacono.rediget.R
import com.diacono.rediget.reader.domain.model.Post
import java.util.concurrent.TimeUnit

class PostRecyclerViewAdapter(
    private val postList: List<Post>,
    private val onClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = postList[position]
        with(holder) {
            name.text = item.name
            creation.text = getTimesInHours(item)
            title.text = item.title
            loadThumbnail(item)
            comments.text = getCommentsAmount(item)
            itemFrame.setOnClickListener(onClickListener)
            itemView.tag = item
        }
    }

    private fun ViewHolder.getCommentsAmount(
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

    private fun ViewHolder.loadThumbnail(
        item: Post
    ) = Glide.with(itemView.context).load(item.thumbnail)
        .fitCenter()
        .into(thumbnail)

    override fun getItemCount() = postList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.vName)
        val creation: TextView = view.findViewById(R.id.vCreation)
        val title: TextView = view.findViewById(R.id.vTitle)
        val thumbnail: ImageView = view.findViewById(R.id.vThumbnail)
        val dismiss: Button = view.findViewById(R.id.vDismiss)
        val comments: TextView = view.findViewById(R.id.vComments)
        val itemFrame :View = view.findViewById(R.id.vItemFrame)
    }
}