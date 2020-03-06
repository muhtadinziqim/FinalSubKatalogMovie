package com.acer.example.katalogfilmsub2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.progressBar
import kotlinx.android.synthetic.main.fragment_tv_show.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        showLoading(true)
        val movie = intent.getParcelableExtra(EXTRA_MOVIE) as Movie

        Glide.with(this)
            .load(movie.poster)
            .apply(RequestOptions().override(200, 300))
            .into(img_poster)
        tv_judul.text = movie.judul;
        tv_desktipsi.text = movie.deskripsi
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
