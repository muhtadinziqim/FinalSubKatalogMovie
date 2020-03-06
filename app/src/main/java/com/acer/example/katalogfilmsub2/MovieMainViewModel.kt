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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MovieMainViewModel : ViewModel() {
    companion object {
        private const val API_KEY = "e5ffacd3e2daf66fcfe8be92718237f5"
    }
    val listMovies = MutableLiveData<ArrayList<Movie>>()

    internal fun setMovie() {
        val listItems = ArrayList<Movie>()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val request: PostService = retrofit.create(PostService::class.java)
        val call: Call<JsonObject> = request.getJson()
        call.enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                  //  val data = response.body()

                    val result = (response.body().toString())
                    Log.d("body", result)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
//                        Log.d("judul : ", movie.getString(("title")))
                        val poster_path = "https://image.tmdb.org/t/p/w185"+movie.getString("poster_path")
                        val movieItems = Movie(movie.getInt("id"), movie.getString("title"), movie.getString("overview"), poster_path)
                        listItems.add(movieItems)
                    }
                    listMovies.postValue(listItems)
                }
            }

            override fun onFailure(call: Call<JsonObject>, error: Throwable) {
                Log.e("failure", "errornya ${error.message}")
            }
        })
    }

    internal fun setMovieSearch(search: String) {
        val listItems = ArrayList<Movie>()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val request: PostService = retrofit.create(PostService::class.java)
        val call: Call<JsonObject> = request.getSearchMovie("e5ffacd3e2daf66fcfe8be92718237f5", "en-US", search)
        call.enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    //  val data = response.body()

                    val result = (response.body().toString())
                    Log.d("body", result)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
//                        Log.d("judul : ", movie.getString(("title")))
                        val poster_path = "https://image.tmdb.org/t/p/w185"+movie.getString("poster_path")
                        val movieItems = Movie(movie.getInt("id"), movie.getString("title"), movie.getString("overview"), poster_path)
                        listItems.add(movieItems)
                    }
                    listMovies.postValue(listItems)
                }
            }

            override fun onFailure(call: Call<JsonObject>, error: Throwable) {
                Log.e("failure", "errornya ${error.message}")
            }
        })
    }

    internal fun setNewRelease() {
        val listItems = ArrayList<Movie>()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val request: PostService = retrofit.create(PostService::class.java)
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val today: String = sdf.format(Date())
        val call: Call<JsonObject> = request.getNewRelease("e5ffacd3e2daf66fcfe8be92718237f5", today, today)
        call.enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    //  val data = response.body()

                    val result = (response.body().toString())
                    Log.d("body", result)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
//                        Log.d("judul : ", movie.getString(("title")))
                        val poster_path = "https://image.tmdb.org/t/p/w185"+movie.getString("poster_path")
                        val movieItems = Movie(movie.getInt("id"), movie.getString("title"), movie.getString("overview"), poster_path)
                        listItems.add(movieItems)
                    }
                    listMovies.postValue(listItems)
                }
            }

            override fun onFailure(call: Call<JsonObject>, error: Throwable) {
                Log.e("failure", "errornya ${error.message}")
            }
        })
    }

    internal fun getmovies(): LiveData<ArrayList<Movie>> {
        return listMovies
    }

}