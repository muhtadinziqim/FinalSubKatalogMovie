package com.acer.example.katalogfilmsub2.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acer.example.katalogfilmsub2.R
import com.acer.example.katalogfilmsub2.TvShow
import com.acer.example.katalogfilmsub2.db.DatabaseHelper
import com.acer.example.katalogfilmsub2.db.FavHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_fav_tv_show.view.*

class TvShowFavAdapter (private val activity: Activity) : RecyclerView.Adapter<TvShowFavAdapter.TvShowViewHolder>() {

    private lateinit var favHelper: FavHelper
    private var tvshow: TvShow? = null
    private var databaseHelper: DatabaseHelper? = null

    private var onItemClickCallback: TvShowFavAdapter.OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: TvShowFavAdapter.OnItemClickCallback) {
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

    var listTvSHow = ArrayList<TvShow>()
        set(listTvShow) {
            if (listTvShow.size > 0) {
                this.listTvSHow.clear()
            }
            this.listTvSHow.addAll(listTvShow)
            notifyDataSetChanged()
        }

    fun addItem(tvShow: TvShow) {
        this.listTvSHow.add(tvShow)
        notifyItemInserted(this.listTvSHow.size - 1)
    }
    fun updateItem(position: Int, tvShow: TvShow) {
        this.listTvSHow[position] = tvShow
        notifyItemChanged(position, tvShow)
    }
    fun removeItem(position: Int) {
        this.listTvSHow.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listTvSHow.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_fav_tv_show, parent, false)
        return TvShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        holder.bind(listTvSHow[position])
    }

    override fun getItemCount(): Int = this.listTvSHow.size


    inner class TvShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvShow: TvShow) {
            with(itemView){
                Glide.with(itemView.context)
                    .load(tvShow.poster)
                    .apply(RequestOptions().override(70, 100))
                    .into(img_item_photo)

                tv_item_judul.text = tvShow.judul
                tv_item_description.text = tvShow.deskripsi

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(tvShow, "detail") }
                btn_hapus.setOnClickListener { onItemClickCallback?.onItemClicked(tvShow, "dihapus") }

            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: TvShow, tipe: String)
    }
}