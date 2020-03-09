package com.acer.example.katalogfilmsub2.favorite_list

import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.acer.example.katalogfilmsub2.*
import com.acer.example.katalogfilmsub2.adapter.MovieFavAdapter
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.acer.example.katalogfilmsub2.db.DatabaseHelper
import com.acer.example.katalogfilmsub2.db.FavHelper
import com.acer.example.katalogfilmsub2.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_movie_fav.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MovieFavActivity : AppCompatActivity() {
    private lateinit var adapter: MovieFavAdapter
    private lateinit var favHelper: FavHelper

    private var position: Int = 0

    private var databaseHelper: DatabaseHelper? = null

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
        const val EXTRA_POSITION = "extra_position"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_fav)

        supportActionBar?.title = "Movie Favorite"

        rv_movies.layoutManager = LinearLayoutManager(this)
        rv_movies.setHasFixedSize(true)
        adapter = MovieFavAdapter(this)
        rv_movies.adapter = adapter

        position = intent.getIntExtra(EXTRA_POSITION, 0)

        adapter.setOnItemClickCallback(object : MovieFavAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Movie, tipe: String) {
                if (tipe == "detail") {
                    val movie = Movie(data.id, data.judul, data.deskripsi, data.poster)
                    val moveDetailActivity= Intent(this@MovieFavActivity, MovieFavDetailActivity::class.java)
                    moveDetailActivity.putExtra(DetailActivity.EXTRA_MOVIE, movie)
                    startActivity(moveDetailActivity)
                    Toast.makeText(
                        this@MovieFavActivity,
                        data.judul + " dipilih",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    databaseHelper = DatabaseHelper(this@MovieFavActivity)
                    databaseHelper!!.deleteFav(data.id)
                    loadMovieAsync()
                    Toast.makeText(
                        this@MovieFavActivity,
                        data.judul + " dihapus",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        favHelper = FavHelper.getInstance(applicationContext)
        favHelper.open()


        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadMovieAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        // proses ambil data
        loadMovieAsync()

        if (savedInstanceState == null) {
            // proses ambil data
            loadMovieAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Movie>(EXTRA_STATE)
            if (list != null) {
                adapter.listMovie = list
            }
        }

    }

    private fun loadMovieAsync() {
        GlobalScope.launch(Dispatchers.Main) {
   //         progressbar.visibility = View.VISIBLE
            val contenUri = Uri.parse(CONTENT_URI.toString() + "/" + "Movie")
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver.query(contenUri, null, null, null, null)
//                val cursor2 = favHelper.queryByCat("Movie")
                MappingHelper.mapCursorToArrayList(cursor)

            }
    //        progressbar.visibility = View.INVISIBLE
            val movie = deferredNotes.await()
            if (movie.size > 0) {
                adapter.listMovie = movie
            } else {
                adapter.listMovie = ArrayList()
                Toast.makeText(this@MovieFavActivity, "Daftar Favorit Kosong", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (data != null) {
//            when (requestCode) {
//
//                MovieFavAdapter.REQUEST_UPDATE ->
//                    when (resultCode) {
//
//                        MovieFavAdapter.RESULT_DELETE -> {
//                            val position = data.getIntExtra(MovieFavAdapter.EXTRA_POSITION, 0)
//                            adapter.removeItem(position)
//                        }
//                    }
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listMovie)
    }
}
