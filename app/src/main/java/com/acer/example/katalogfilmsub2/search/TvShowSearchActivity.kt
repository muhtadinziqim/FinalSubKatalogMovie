package com.acer.example.katalogfilmsub2.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.acer.example.katalogfilmsub2.*
import kotlinx.android.synthetic.main.activity_tv_show_search.*

class TvShowSearchActivity : AppCompatActivity() {

    private lateinit var adapter: ListTvShowAdapter
    private lateinit var tvshowMainViewModel: TvShowMainModelView

    companion object{
        const val EXTRA_SEARCH = "seacrh"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show_search)

        rv_tv_show.setHasFixedSize(true)

//        list.addAll(getListMovies())
        showRecyclerList()
    }

    private fun showRecyclerList() {

        adapter = ListTvShowAdapter()
        adapter.notifyDataSetChanged()

        rv_tv_show.layoutManager = LinearLayoutManager(this@TvShowSearchActivity)
//        val listMovieAdapter = ListMovieAdapter(list)
        rv_tv_show.adapter = adapter

        tvshowMainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(TvShowMainModelView::class.java)
        tvshowMainViewModel.setTvShowSearch(intent.getStringExtra(EXTRA_SEARCH))
        showLoading(true)
        tvshowMainViewModel.getTvShow().observe(this, androidx.lifecycle.Observer { movie ->
            if (movie != null){
                adapter.setData(movie)
                showLoading(false)
            }
        })

        adapter.setOnItemClickCallback(object : ListTvShowAdapter.OnItemClickCallback{
            override fun onItemClicked(data: TvShow) {
                showSelectedMovie(data)
            }
        })
    }

    private fun showSelectedMovie(tvshow: TvShow) {
        Toast.makeText(this@TvShowSearchActivity, "Kamu memilih ${tvshow.judul}", Toast.LENGTH_SHORT).show()

        val tvShow = TvShow(tvshow.id, tvshow.judul, tvshow.deskripsi, tvshow.poster)
        val moveDetailActivity= Intent(this@TvShowSearchActivity, DetailTvShowActivity::class.java)
        moveDetailActivity.putExtra(DetailTvShowActivity.EXTRA_TV_SHOW, tvShow)
        startActivity(moveDetailActivity)
    }

    private fun showLoading(state: Boolean){
        if (state){
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.GONE
        }
    }
}
