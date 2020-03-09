package com.acer.example.favoritmovie

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_movie.view.*

class MovieAdapter() : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var listNotes = java.util.ArrayList<Movie>()
        set(listNotes) {
            this.listNotes.clear()
            this.listNotes.addAll(listNotes)
            notifyDataSetChanged()
        }

    var listMovie = ArrayList<Movie>()
        set(listMovie) {
            if (listMovie.size > 0) {
                this.listMovie.clear()
            }
            this.listMovie.addAll(listMovie)
            notifyDataSetChanged()
        }

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

            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)
    }

}