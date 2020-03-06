package com.acer.example.katalogfilmsub2.alarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.acer.example.katalogfilmsub2.*
import kotlinx.android.synthetic.main.activity_new_release.*

class NewReleaseActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter
    private lateinit var movieMainViewModel: MovieMainViewModel

    companion object{
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_release)

        rv_movies.setHasFixedSize(true)

//        list.addAll(getListMovies())
        showRecyclerList()
    }

    private fun showRecyclerList() {

        adapter = MovieAdapter()
        adapter.notifyDataSetChanged()

        rv_movies.layoutManager = LinearLayoutManager(this@NewReleaseActivity)
//        val listMovieAdapter = ListMovieAdapter(list)
        rv_movies.adapter = adapter

        movieMainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MovieMainViewModel::class.java)
        movieMainViewModel.setNewRelease()
        showLoading(true)
        movieMainViewModel.getmovies().observe(this, androidx.lifecycle.Observer { movie ->
            if (movie != null){
                adapter.setData(movie)
                showLoading(false)
            }
        })

        adapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Movie) {
                showSelectedMovie(data)
            }
        })
    }

    private fun showSelectedMovie(movie: Movie) {
        Toast.makeText(this@NewReleaseActivity, "Kamu memilih ${movie.judul}", Toast.LENGTH_SHORT).show()

        val movie = Movie(movie.id, movie.judul, movie.deskripsi, movie.poster)
        val moveDetailActivity= Intent(this@NewReleaseActivity, DetailActivity::class.java)
        moveDetailActivity.putExtra(DetailActivity.EXTRA_MOVIE, movie)
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
