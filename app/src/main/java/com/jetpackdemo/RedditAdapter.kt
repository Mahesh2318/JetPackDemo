package com.jetpackdemo

import android.arch.paging.PagedListAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.adapter_row.view.*

class RedditAdapter : PagedListAdapter<RedditPost, RedditViewHolder>(RedditDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_row, parent, false)
        return RedditViewHolder(view)
    }

    override fun onBindViewHolder(holder: RedditViewHolder, position: Int) {

        val item = getItem(position)
        val resources = holder.itemView.context.resources

        val scoreString = resources.getString(R.string.score, item?.score)
        val commentCountString = resources.getString(R.string.comments, item?.commentCount)

        holder.itemView.title.text = item?.title
        holder.itemView.score.text = scoreString

        holder.itemView.comments.text = commentCountString
    }
}
