package com.acer.example.katalogfilmsub2

import android.provider.Settings.Global.getString
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface PostService {

    @GET("3/discover/movie?api_key=e5ffacd3e2daf66fcfe8be92718237f5&language=en-US")
    fun getJson(): Call<JsonObject>

    @GET("3/discover/tv?api_key=e5ffacd3e2daf66fcfe8be92718237f5&language=en-US")
    fun getJsonTvShow(): Call<JsonObject>

    @GET("3/search/movie?")
    fun getSearchMovie(@Query("api_key") api_key: String, @Query("language") language: String, @Query("query") name: String ): Call<JsonObject>

    @GET("3/search/tv?")
    fun getSearchTvShow(@Query("api_key") api_key: String, @Query("language") language: String, @Query("query") name: String ): Call<JsonObject>


    @GET("3/discover/movie?")
    fun getNewRelease(@Query("api_key") api_key: String, @Query("primary_release_date.gte") primary_release_date: String, @Query("primary_release_date.lte") primary_release_date_lte: String ): Call<JsonObject>

}