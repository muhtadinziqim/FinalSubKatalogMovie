package com.acer.example.katalogfilmsub2

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.acer.example.katalogfilmsub2.db.CustomOnItemClickListener
import com.acer.example.katalogfilmsub2.db.DatabaseContract
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.DESKRIPSI
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.JUDUL
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.KATEGORI
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.POSTER
import com.acer.example.katalogfilmsub2.db.DatabaseHelper
import com.acer.example.katalogfilmsub2.db.FavHelper
import com.acer.example.katalogfilmsub2.helper.MappingHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_movie.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.log

class MovieAdapter() : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private lateinit var favHelper: FavHelper
    private var databaseHelper: DatabaseHelper? = null

    private var onItemClickCallback: MovieAdapter.OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: MovieAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private val mData = ArrayList<Movie>()
    fun setData(items: ArrayList<Movie>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MovieViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_movie, viewGroup, false)
        return MovieViewHolder(mView)
    }


    override fun onBindViewHolder(weatherViewHolder: MovieViewHolder, position: Int) {
        weatherViewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movieItems: Movie) {
            with(itemView){
                Glide.with(itemView.context)
                    .load(movieItems.poster)
                    .apply(RequestOptions().override(70, 100))
                    .into(img_item_photo)

                tv_item_judul.text = movieItems.judul
                tv_item_description.text = movieItems.deskripsi

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(movieItems) }

                btn_set_favorite.setOnClickListener (CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {



                        databaseHelper = DatabaseHelper(context)

                        var sudahada = cekSudahAda(movieItems.id, databaseHelper)
                        if (sudahada){
                            Toast.makeText(context, movieItems.judul + "Sudah ada di favorit", Toast.LENGTH_SHORT).show()

                        }else {
                            databaseHelper!!.addMovieFav(
                                movieItems.id,
                                movieItems.judul,
                                movieItems.deskripsi,
                                movieItems.poster,
                                "Movie"
                            )

                            Toast.makeText(
                                context,
                                movieItems.judul + " Favorit",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }))
            }
        }
    }

    private fun cekSudahAda(id: Int, dbHelp: DatabaseHelper?) : Boolean{
        databaseHelper = dbHelp
        var ret = false

        val movie = databaseHelper!!.queryById(id)
        if (movie.size > 0) {
            ret = true
        } else {
            ret = false
        }

        return ret
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)
    }

}