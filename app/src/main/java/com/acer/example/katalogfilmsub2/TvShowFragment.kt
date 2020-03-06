package com.acer.example.katalogfilmsub2


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.acer.example.katalogfilmsub2.alarm.PengaturanReminderActivity
import com.acer.example.katalogfilmsub2.favorite_list.MovieFavActivity
import com.acer.example.katalogfilmsub2.favorite_list.TvShowFavActivity
import com.acer.example.katalogfilmsub2.search.TvShowSearchActivity
import kotlinx.android.synthetic.main.fragment_tv_show.*


/**
 * A simple [Fragment] subclass.
 */
class TvShowFragment : Fragment() {

    private val list = ArrayList<TvShow>()

    private lateinit var adapter: ListTvShowAdapter
    private lateinit var tvShowMainViewModel: TvShowMainModelView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_tv_show.setHasFixedSize(true)
        setHasOptionsMenu(true);
        showRecyclerList()

    }


    private fun showRecyclerList() {

        adapter = ListTvShowAdapter()
        adapter.notifyDataSetChanged()

        rv_tv_show.layoutManager = LinearLayoutManager(this.context)
        rv_tv_show.adapter = adapter

        tvShowMainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(TvShowMainModelView::class.java)
        tvShowMainViewModel.setTvShow()
        showLoading(true)
        tvShowMainViewModel.getTvShow().observe(this, androidx.lifecycle.Observer { tvshow ->
            if (tvshow != null){
                adapter.setData(tvshow)
                showLoading(false)
            }
        })

       adapter.setOnItemClickCallback(object : ListTvShowAdapter.OnItemClickCallback{
            override fun onItemClicked(data: TvShow) {
                showSelectedTvShow(data)
            }
        })
    }

    private fun showSelectedTvShow(tvshow: TvShow) {
        Toast.makeText(this.context, "Kamu memilih ${tvshow.judul}", Toast.LENGTH_SHORT).show()

        val tvshow = TvShow(tvshow.id, tvshow.judul, tvshow.deskripsi, tvshow.poster)
        val moveDetailTvShowActivity= Intent(this.context, DetailTvShowActivity::class.java)
        moveDetailTvShowActivity.putExtra(DetailTvShowActivity.EXTRA_TV_SHOW, tvshow)
        startActivity(moveDetailTvShowActivity)
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
//            Toast.makeText(this, "Menu Fav Movie", Toast.LENGTH_LONG).show()
            val favMovieIntent = Intent(context, MovieFavActivity::class.java)
            startActivity(favMovieIntent)
        }
        if (item.itemId == R.id.view_fav_tvshow) {
//            Toast.makeText(this, "Menu Fav TvShow", Toast.LENGTH_LONG).show()
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
                            Toast.makeText(context, "Film Search "+ query, Toast.LENGTH_SHORT).show()
                            val intentSeachTv = Intent(context, TvShowSearchActivity::class.java)
                            intentSeachTv.putExtra(TvShowSearchActivity.EXTRA_SEARCH, query)
                            startActivity(intentSeachTv)
                            Log.d("Search", query)
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
