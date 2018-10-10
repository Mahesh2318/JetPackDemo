
package com.jetpackdemo

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class RedditPost(
    @SerializedName("name")
    val key: String,
    @SerializedName("title")
    @PrimaryKey
    val title: String,
    @SerializedName("score")
    val score: Int,
    @SerializedName("author")
    val author: String,
    @SerializedName("num_comments")
    val commentCount: Int
)
