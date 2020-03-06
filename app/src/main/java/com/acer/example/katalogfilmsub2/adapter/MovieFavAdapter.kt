package com.acer.example.katalogfilmsub2.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acer.example.katalogfilmsub2.Movie
import com.acer.example.katalogfilmsub2.R
import com.acer.example.katalogfilmsub2.db.DatabaseHelper
import com.acer.example.katalogfilmsub2.db.FavHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_fav_movie.view.*
import kotlinx.android.synthetic.main.item_row_fav_movie.view.img_item_photo
import kotlinx.android.synthetic.main.item_row_fav_movie.view.tv_item_description
import kotlinx.android.synthetic.main.item_row_fav_movie.view.tv_item_judul

class MovieFavAdapter(private val activity: Activity) : RecyclerView.Adapter<MovieFavAdapter.MovieViewHolder>() {

    private lateinit var favHelper: FavHelper
    private var movie: Movie? = null
    private var databaseHelper: DatabaseHelper? = null

    private var onItemClickCallback: MovieFavAdapter.OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: MovieFavAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    var listMovie = ArrayList<Movie>()
        set(listMovie) {
            if (listMovie.size > 0) {
                this.listMovie.clear()
            }
            this.listMovie.addAll(listMovie)
            notifyDataSetChanged()
        }

    fun addItem(movie: Movie) {
        this.listMovie.add(movie)
        notifyItemInserted(this.listMovie.size - 1)
    }
    fun updateItem(position: Int, movie: Movie) {
        this.listMovie[position] = movie
        notifyItemChanged(position, movie)
    }
    fun removeItem(position: Int) {
        this.listMovie.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listMovie.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_fav_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(listMovie[position])
    }
    override fun getItemCount(): Int = this.listMovie.size


    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView){
                Glide.with(itemView.context)
                    .load(movie.poster)
                    .apply(RequestOptions().override(70, 100))
                    .into(img_item_photo)

                tv_item_judul.text = movie.judul
                tv_item_description.text = movie.deskripsi

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(movie, "detail") }
                btn_hapus.setOnClickListener { onItemClickCallback?.onItemClicked(movie, "dihapus") }

//                btn_hapus.setOnClickListener (CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
//                    override fun onItemClicked(view: View, position: Int) {
//
//                        databaseHelper = DatabaseHelper(context)
//                        databaseHelper!!.deleteFav(movie.judul)
//                        Toast.makeText(context, "Berhasil Dihapus", Toast.LENGTH_SHORT).show()
//                        val favMovieIntent = Intent(context, MovieFavActivity::class.java)
////                        startActivity(favMovieIntent)
//                        val intent = Intent()
//                        intent.putExtra(EXTRA_POSITION, position)
////                        setResult(RESULT_DELETE, intent)
////                        finish()
//                    }
//                }))
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie, tipe: String)
    }
}