package com.diacono.rediget.reader.presentation

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diacono.rediget.R
import com.diacono.rediget.reader.domain.model.Post
import com.diacono.rediget.reader.presentation.detail.PostDetailActivity
import com.diacono.rediget.reader.presentation.detail.PostDetailFragment

class PostRecyclerViewAdapter(
    private val parentActivity: PostListActivity,
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
        holder.idView.text = item.name
        holder.contentView.text = item.title

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = postList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.id_text)
        val contentView: TextView = view.findViewById(R.id.content)
    }
}