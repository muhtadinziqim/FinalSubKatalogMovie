package com.acer.example.favoritmovie


import android.content.Intent
import android.content.ContentResolver
import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import com.acer.example.favoritmovie.DatabaseContract.FavColumns.Companion.CONTENT_URI


/**
 * A simple [Fragment] subclass.
 */
class MoviesFragment : Fragment() {

    private val list = ArrayList<Movie>()


    private lateinit var adapter: MovieAdapter
    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        favHelper = FavHelper.getInstance(context)
        rv_movies.setHasFixedSize(true)

        val contentResolver = activity!!.contentResolver
//        list.addAll(getListMovies())
        showRecyclerList(contentResolver)

        if (savedInstanceState == null) {
            loadNotesAsync(contentResolver)
        } else {
            val list = savedInstanceState.getParcelableArrayList<Movie>(EXTRA_STATE)
            if (list != null) {
                adapter.listNotes = list
            }
        }
    }

//
    private fun showRecyclerList(contentResolver: ContentResolver) {

        adapter = MovieAdapter()
        adapter.notifyDataSetChanged()

        rv_movies.layoutManager = LinearLayoutManager(this.context)
//        val listMovieAdapter = ListMovieAdapter(list)
        rv_movies.adapter = adapter

//        Load data
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync(contentResolver)
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        adapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Movie) {
                showSelectedMovie(data)
            }
        })
    }

    private fun showSelectedMovie(movie: Movie) {
        Toast.makeText(this.context, "Kamu memilih ${movie.judul}", Toast.LENGTH_SHORT).show()

        val movie = Movie(movie.id, movie.judul, movie.deskripsi, movie.poster)
        val moveDetailActivity= Intent(this.context, DetailActivity::class.java)
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

    private fun loadNotesAsync(contentResolver: ContentResolver) {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null) as Cursor
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val notes = deferredNotes.await()
            progressBar.visibility = View.INVISIBLE
            if (notes.size > 0) {
                adapter.listNotes = notes
            } else {
                adapter.listNotes = ArrayList()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

}
