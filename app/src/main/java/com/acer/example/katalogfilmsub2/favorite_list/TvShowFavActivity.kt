package com.acer.example.katalogfilmsub2.favorite_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.acer.example.katalogfilmsub2.DetailActivity
import com.acer.example.katalogfilmsub2.R
import com.acer.example.katalogfilmsub2.TvShow
import com.acer.example.katalogfilmsub2.adapter.TvShowFavAdapter
import com.acer.example.katalogfilmsub2.db.DatabaseHelper
import com.acer.example.katalogfilmsub2.db.FavHelper
import com.acer.example.katalogfilmsub2.favorite_list.TvShowFavDetailActivity.Companion.EXTRA_TVSHOW
import com.acer.example.katalogfilmsub2.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_tv_show_fav.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TvShowFavActivity : AppCompatActivity() {

    private lateinit var adapter: TvShowFavAdapter
    private lateinit var favHelper: FavHelper

    private var position: Int = 0

    private var databaseHelper: DatabaseHelper? = null

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
        const val EXTRA_POSITION = "extra_position"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show_fav)

        supportActionBar?.title = "Tv SHow Favorite"

        rv_tv_show.layoutManager = LinearLayoutManager(this)
        rv_tv_show.setHasFixedSize(true)
        adapter = TvShowFavAdapter(this)
        rv_tv_show.adapter = adapter

        position = intent.getIntExtra(EXTRA_POSITION, 0)

        adapter.setOnItemClickCallback(object : TvShowFavAdapter.OnItemClickCallback{
            override fun onItemClicked(data: TvShow, tipe: String) {
                if (tipe == "detail") {
                    val tvShow = TvShow(data.id, data.judul, data.deskripsi, data.poster)
                    val moveDetailActivity= Intent(this@TvShowFavActivity, TvShowFavDetailActivity::class.java)
                    moveDetailActivity.putExtra(EXTRA_TVSHOW, tvShow)
                    startActivity(moveDetailActivity)
                    Toast.makeText(
                        this@TvShowFavActivity,
                        data.judul + " dipilih",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    databaseHelper = DatabaseHelper(this@TvShowFavActivity)
                    databaseHelper!!.deleteFav(data.id)
//                    val intent = Intent()
//                    intent.putExtra(EXTRA_POSITION, position)
//                    setResult(RESULT_DELETE, intent)
//                    finish()
                    loadMovieAsync()
                    Toast.makeText(
                        this@TvShowFavActivity,
                        data.judul + " dihapus",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        favHelper = FavHelper.getInstance(applicationContext)
        favHelper.open()

        // proses ambil data
        loadMovieAsync()

        if (savedInstanceState == null) {
            // proses ambil data
            loadMovieAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<TvShow>(EXTRA_STATE)
            if (list != null) {
                adapter.listTvSHow = list
            }
        }
    }

    private fun loadMovieAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            //         progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = favHelper.queryByCat("TvShow")
                MappingHelper.mapCursorToArrayListTvShow(cursor)
            }
            //        progressbar.visibility = View.INVISIBLE
            val movie = deferredNotes.await()
            if (movie.size > 0) {
                adapter.listTvSHow = movie
            } else {
                adapter.listTvSHow = ArrayList()
                Toast.makeText(this@TvShowFavActivity, "Daftar Favorit Kosong", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
