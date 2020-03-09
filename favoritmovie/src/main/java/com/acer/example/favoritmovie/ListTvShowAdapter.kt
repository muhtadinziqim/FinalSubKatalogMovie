package com.acer.example.favoritmovie

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_tv_show.view.img_item_photo
import kotlinx.android.synthetic.main.item_row_tv_show.view.tv_item_description
import kotlinx.android.synthetic.main.item_row_tv_show.view.tv_item_judul

class ListTvShowAdapter(): RecyclerView.Adapter<ListTvShowAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    var listNotes = java.util.ArrayList<TvShow>()
        set(listNotes) {
            this.listNotes.clear()
            this.listNotes.addAll(listNotes)
            notifyDataSetChanged()
        }

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

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(tvshow) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: TvShow)
    }
}