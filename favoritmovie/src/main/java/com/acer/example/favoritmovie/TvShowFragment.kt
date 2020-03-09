package com.acer.example.favoritmovie


import android.app.SearchManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
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
import com.acer.example.favoritmovie.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.acer.example.katalogfilmsub2.DetailTvShowActivity
import kotlinx.android.synthetic.main.fragment_tv_show.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
class TvShowFragment : Fragment() {

    private val list = ArrayList<TvShow>()
    private lateinit var uriWithCat: Uri

    private lateinit var adapter: ListTvShowAdapter
    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

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
        val contentResolver = activity!!.contentResolver
        uriWithCat = Uri.parse(CONTENT_URI.toString() + "/" + "TvShow")
//        list.addAll(getListMovies())
        showRecyclerList(contentResolver, uriWithCat)

        if (savedInstanceState == null) {
            loadNotesAsync(contentResolver, uriWithCat)
        } else {
            val list = savedInstanceState.getParcelableArrayList<TvShow>(EXTRA_STATE)
            if (list != null) {
                adapter.listNotes = list
            }
        }

    }


    private fun showRecyclerList(contentResolver: ContentResolver, uri: Uri) {

        adapter = ListTvShowAdapter()
        adapter.notifyDataSetChanged()

        rv_tv_show.layoutManager = LinearLayoutManager(this.context)
        rv_tv_show.adapter = adapter

//      Load data
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync(contentResolver, uri)
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)


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

    private fun loadNotesAsync(contentResolver: ContentResolver, uri: Uri) {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver.query(uri, null, null, null, null)
                Log.d("Uri", uri.toString())
                MappingHelper.mapCursorToArrayListTvShow(cursor)
            }
            val notes = deferredNotes.await()
            progressBar.visibility = View.INVISIBLE
            if (notes.size > 0) {
                adapter.listNotes = notes
            } else {
                adapter.listNotes = ArrayList()
            }
            Log.d("list", notes.toString())
        }
    }

}
