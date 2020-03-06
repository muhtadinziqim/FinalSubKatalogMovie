package com.acer.example.katalogfilmsub2


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.acer.example.katalogfilmsub2.alarm.PengaturanReminderActivity
import com.acer.example.katalogfilmsub2.db.DatabaseHelper
import com.acer.example.katalogfilmsub2.favorite_list.MovieFavActivity
import com.acer.example.katalogfilmsub2.favorite_list.TvShowFavActivity
import com.acer.example.katalogfilmsub2.search.MovieSearchActivity
import kotlinx.android.synthetic.main.fragment_movies.*


/**
 * A simple [Fragment] subclass.
 */
class MoviesFragment : Fragment() {

    private val list = ArrayList<Movie>()

    private lateinit var adapter: MovieAdapter
    private lateinit var movieMainViewModel: MovieMainViewModel


    private var databaseHelper: DatabaseHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())
//        favHelper = FavHelper.getInstance(context)
        rv_movies.setHasFixedSize(true)
        setHasOptionsMenu(true);

//        list.addAll(getListMovies())
        showRecyclerList()
    }

//
    private fun showRecyclerList() {

        adapter = MovieAdapter()
        adapter.notifyDataSetChanged()

        rv_movies.layoutManager = LinearLayoutManager(this.context)
//        val listMovieAdapter = ListMovieAdapter(list)
        rv_movies.adapter = adapter

        movieMainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MovieMainViewModel::class.java)
        movieMainViewModel.setMovie()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        if (item.itemId == R.id.view_fav_movie) {
            val favMovieIntent = Intent(context, MovieFavActivity::class.java)
            startActivity(favMovieIntent)
        }
        if (item.itemId == R.id.view_fav_tvshow) {
            val favTvShowIntent = Intent(context, TvShowFavActivity::class.java)
            startActivity(favTvShowIntent)
        }
        if (item.itemId == R.id.setting_reminder) {
            val settingReminder = Intent(context, PengaturanReminderActivity::class.java)
            startActivity(settingReminder)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchManager: SearchManager
        if (context != null) {
            searchManager =
                context!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            if (searchManager != null) {
                val searchView =
                    menu.findItem(R.id.search).actionView as SearchView
                if (activity != null) {
                    searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
                }
                val queryTextListener: SearchView.OnQueryTextListener =
                    object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean {
                            Toast.makeText(context, "Movie Search"+ query, Toast.LENGTH_SHORT).show()
                            val intentSeachMovie = Intent(context, MovieSearchActivity::class.java)
                            intentSeachMovie.putExtra(MovieSearchActivity.EXTRA_SEARCH, query)
                            startActivity(intentSeachMovie)
                            return true
                        }

                        override fun onQueryTextChange(newText: String): Boolean {
                            return false
                        }
                    }
                searchView.setOnQueryTextListener(queryTextListener)
            }
            super.onPrepareOptionsMenu(menu)
        }
    }

}
