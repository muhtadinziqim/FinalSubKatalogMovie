package com.acer.example.favoritmovie


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
import com.acer.example.katalogfilmsub2.DetailTvShowActivity
import kotlinx.android.synthetic.main.fragment_tv_show.*


/**
 * A simple [Fragment] subclass.
 */
class TvShowFragment : Fragment() {

    private val list = ArrayList<TvShow>()

    private lateinit var adapter: ListTvShowAdapter

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

//      Load data

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

}
