package com.acer.example.katalogfilmsub2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TvShowMainModelView : ViewModel() {
    companion object {
        private const val API_KEY = "e5ffacd3e2daf66fcfe8be92718237f5"
    }
    val listTvShow = MutableLiveData<ArrayList<TvShow>>()


    internal fun setTvShow() {
        val listItems = ArrayList<TvShow>()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val request: PostService = retrofit.create(PostService::class.java)
        val call: Call<JsonObject> = request.getJsonTvShow()
        call.enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                  //  val data = response.body()

                    val result = (response.body().toString())
                    Log.d("body", result)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val tvShow = list.getJSONObject(i)
                        val poster_path = "https://image.tmdb.org/t/p/w185"+ tvShow.getString("poster_path")
                        val tvShowItems = TvShow(tvShow.getInt("id"), tvShow.getString("name"), tvShow.getString("overview"), poster_path)
                        listItems.add(tvShowItems)
                    }
                    listTvShow.postValue(listItems)
                }
            }

            override fun onFailure(call: Call<JsonObject>, error: Throwable) {
                Log.e("failure", "errornya ${error.message}")
            }
        })
    }

    internal fun setTvShowSearch(search: String) {
        val listItems = ArrayList<TvShow>()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val request: PostService = retrofit.create(PostService::class.java)
        val call: Call<JsonObject> = request.getSearchTvShow("e5ffacd3e2daf66fcfe8be92718237f5", "en-US", search)
        call.enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    //  val data = response.body()

                    val result = (response.body().toString())
                    Log.d("body", result)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val tvShow = list.getJSONObject(i)
                        val poster_path = "https://image.tmdb.org/t/p/w185"+ tvShow.getString("poster_path")
                        val tvShowItems = TvShow(tvShow.getInt("id"), tvShow.getString("name"), tvShow.getString("overview"), poster_path)
                        listItems.add(tvShowItems)
                    }
                    listTvShow.postValue(listItems)
                }
            }

            override fun onFailure(call: Call<JsonObject>, error: Throwable) {
                Log.e("failure", "errornya ${error.message}")
            }
        })
    }

    internal fun getTvShow(): LiveData<ArrayList<TvShow>> {
        return listTvShow
    }

}