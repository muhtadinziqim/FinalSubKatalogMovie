package com.acer.example.katalogfilmsub2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.acer.example.favoritmovie.R
import com.acer.example.favoritmovie.TvShow
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail_tv_show.*
import kotlinx.android.synthetic.main.activity_detail_tv_show.progressBar
import kotlinx.android.synthetic.main.fragment_tv_show.*

class DetailTvShowActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TV_SHOW = "extra_tv"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tv_show)
        showLoading(true)

        val tvshow = intent.getParcelableExtra(EXTRA_TV_SHOW) as TvShow

        Glide.with(this)
            .load(tvshow.poster)
            .apply(RequestOptions().override(200, 300))
            .into(img_poster)
        tv_judul.text = tvshow.judul;
        tv_desktipsi.text = tvshow.deskripsi
        showLoading(false)
    }

    private fun showLoading(state: Boolean){
        if (state){
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.GONE
        }
    }
}
