package com.acer.example.katalogfilmsub2.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.acer.example.katalogfilmsub2.FilmStackWidget
import com.acer.example.katalogfilmsub2.Movie
import com.acer.example.katalogfilmsub2.R
import com.acer.example.katalogfilmsub2.db.FavHelper
import com.acer.example.katalogfilmsub2.helper.MappingHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private var mWidgetItems = ArrayList<Movie>()

    private lateinit var favHelper: FavHelper


    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(i: Int): Long = 0

    override fun onDataSetChanged() {

        favHelper = FavHelper.getInstance(mContext)
        favHelper.open()
        loadMovie()
        Log.d("changed movie", mWidgetItems.toString())
//        for (movie in mWidgetItems){
//            mWidgetItems.add(movie)
//            Log.d("movie ls", movie.judul)
//        }

//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.storm_trooper))
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.starwars))
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.falcon))
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
//        rv.setImageViewBitmap(R.id.img_poster, mWidgetItems[position])
        val movieResults = mWidgetItems
        try {
            Log.d("ini dia", movieResults[position].judul)
            val bmp: Bitmap = Glide.with(mContext)
                .asBitmap()
                .load(movieResults[position].poster)
                .apply(RequestOptions().fitCenter())
                .submit()
                .get()
            rv.setImageViewBitmap(R.id.img_item_photo, bmp)
            rv.setTextViewText(R.id.tv_item_judul, movieResults[position].judul)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val extras = bundleOf(
            FilmStackWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.img_poster, fillInIntent)
        return rv
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {

    }

    private fun loadMovieAsync() {
        var listMovie = ArrayList<Movie>()
        GlobalScope.launch(Dispatchers.Main) {
            //         progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = favHelper.queryByCat("Movie")
                MappingHelper.mapCursorToArrayList(cursor)
            }
            //        progressbar.visibility = View.INVISIBLE
            val movie = deferredNotes.await()

            if (movie.size > 0) {
                listMovie = movie
            } else {
                listMovie = movie
            }
            Log.d("list movie", listMovie.toString())
        }
        for (movie in listMovie){
            mWidgetItems.add(movie)
        }
    }

    private fun loadMovie(){
        val cursor = favHelper.queryByCat("Movie")
        val res = MappingHelper.mapCursorToArrayList(cursor)
        mWidgetItems = res;
    }
}