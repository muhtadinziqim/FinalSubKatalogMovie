package com.acer.example.katalogfilmsub2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_movie.view.*

class ListMovieAdapter(private val listMovie: ArrayList<Movie>): RecyclerView.Adapter<ListMovieAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_movie, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listMovie.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMovie[position])
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView){
                Glide.with(itemView.context)
                    .load(movie.poster)
                    .apply(RequestOptions().override(70, 100))
                    .into(img_item_photo)
                tv_item_judul.text = movie.judul
                tv_item_description.text = movie.deskripsi

            //    itemView.setOnClickListener {  }
                btn_set_favorite.setOnClickListener { Toast.makeText(itemView.context, "Favorite ${movie.judul}", Toast.LENGTH_SHORT).show() }

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(movie) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)
    }
}