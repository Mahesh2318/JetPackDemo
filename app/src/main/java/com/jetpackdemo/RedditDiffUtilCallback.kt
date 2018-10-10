package com.jetpackdemo

import android.support.v7.util.DiffUtil

class RedditDiffUtilCallback : DiffUtil.ItemCallback<RedditPost>() {

  override fun areItemsTheSame(oldItem: RedditPost?, newItem: RedditPost?): Boolean {
    return oldItem?.key == newItem?.key
  }

  override fun areContentsTheSame(oldItem: RedditPost?, newItem: RedditPost?): Boolean {

    return oldItem?.title == newItem?.title
      && oldItem?.score == newItem?.score
      && oldItem?.commentCount == newItem?.commentCount
  }
}
