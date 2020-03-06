package com.acer.example.katalogfilmsub2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.acer.example.katalogfilmsub2.db.CustomOnItemClickListener
import com.acer.example.katalogfilmsub2.db.DatabaseHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_movie.view.*
import kotlinx.android.synthetic.main.item_row_tv_show.view.*
import kotlinx.android.synthetic.main.item_row_tv_show.view.btn_set_favorite
import kotlinx.android.synthetic.main.item_row_tv_show.view.img_item_photo
import kotlinx.android.synthetic.main.item_row_tv_show.view.tv_item_description
import kotlinx.android.synthetic.main.item_row_tv_show.view.tv_item_judul

class ListTvShowAdapter(): RecyclerView.Adapter<ListTvShowAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    private var databaseHelper: DatabaseHelper? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private val mData = ArrayList<TvShow>()
    fun setData(items: ArrayList<TvShow>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_tv_show, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tvshow: TvShow) {
            with(itemView){
                Glide.with(itemView.context)
                    .load(tvshow.poster)
                    .apply(RequestOptions().override(70, 100))
                    .into(img_item_photo)
                tv_item_judul.text = tvshow.judul
                tv_item_description.text = tvshow.deskripsi

                btn_set_favorite.setOnClickListener (CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        databaseHelper = DatabaseHelper(context)

                        var sudahada = cekSudahAda(tvshow.id, databaseHelper)
                        if (sudahada){
                            Toast.makeText(context, tvshow.judul + "Sudah ada di favorit", Toast.LENGTH_SHORT).show()

                        }else {

                            databaseHelper!!.addMovieFav(
                                tvshow.id,
                                tvshow.judul,
                                tvshow.deskripsi,
                                tvshow.poster,
                                "TvShow"
                            )

                            Toast.makeText(context, tvshow.judul + " Favorit", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }))

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(tvshow) }
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
        fun onItemClicked(data: TvShow)
    }
}