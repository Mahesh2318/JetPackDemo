package com.jetpackdemo

import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  private val adapter = RedditAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initializeList()
  }

  private fun initializeList() {
    list.layoutManager = LinearLayoutManager(this)
    list.adapter = adapter

    //1
    val config = PagedList.Config.Builder()
        .setPageSize(30)
        .setEnablePlaceholders(false)
        .build()

    //2
    val liveData = initializedPagedListBuilder(config).build()

    //3
    liveData.observe(this, Observer<PagedList<RedditPost>> { pagedList ->
      adapter.submitList(pagedList)
    })
  }

  private fun initializedPagedListBuilder(config: PagedList.Config):
      LivePagedListBuilder<Int, RedditPost> {

    val database = RedditDb.create(this)

    val livePageListBuilder = LivePagedListBuilder<Int, RedditPost>(
        database.postDao().posts(),
        config)

    livePageListBuilder.setBoundaryCallback(RedditBoundaryCallback(database))

    return livePageListBuilder
  }
}
