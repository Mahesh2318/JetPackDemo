
package com.jetpackdemo

import android.arch.paging.PagedList
import android.util.Log
import com.jetpackdemo.utils.PagingRequestHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class RedditBoundaryCallback(private val db: RedditDb) : PagedList.BoundaryCallback<RedditPost>() {

  private val api = RedditService.createService()
  private val executor = Executors.newSingleThreadExecutor()
  private val helper = PagingRequestHelper(executor)

  override fun onZeroItemsLoaded() {
    super.onZeroItemsLoaded()
    //1
    helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { helperCallback ->
      api.getPosts()
          //2
          .enqueue(object : Callback<RedditApiResponse> {

            override fun onFailure(call: Call<RedditApiResponse>?, t: Throwable) {
              //3
              Log.e("RedditBoundaryCallback", "Failed to load data!")
              helperCallback.recordFailure(t)
            }

            override fun onResponse(
                call: Call<RedditApiResponse>?,
                response: Response<RedditApiResponse>) {
              //4
              val posts = response.body()?.data?.children?.map { it.data }
                Log.e("runIfNotRunning", "onResponse :"+posts.toString())
              executor.execute {
                db.postDao().insert(posts ?: listOf())
                helperCallback.recordSuccess()
              }
            }
          })
    }
  }

  override fun onItemAtEndLoaded(itemAtEnd: RedditPost) {
    super.onItemAtEndLoaded(itemAtEnd)
    helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { helperCallback ->
      api.getPosts(after = itemAtEnd.key)
          .enqueue(object : Callback<RedditApiResponse> {

            override fun onFailure(call: Call<RedditApiResponse>?, t: Throwable) {
              Log.e("RedditBoundaryCallback", "Failed to load data!")
              helperCallback.recordFailure(t)
            }

            override fun onResponse(
                call: Call<RedditApiResponse>?,
                response: Response<RedditApiResponse>) {

              val posts = response.body()?.data?.children?.map {
                  it.data
              }
                Log.e("onItemAtEndLoaded", "onResponse :"+posts.toString())
              executor.execute {
                db.postDao().insert(posts ?: listOf())
                helperCallback.recordSuccess()
              }
            }
          })
    }
  }
}
