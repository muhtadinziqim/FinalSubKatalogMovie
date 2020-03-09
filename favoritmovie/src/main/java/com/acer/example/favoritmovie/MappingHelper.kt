package com.acer.example.favoritmovie

import android.database.Cursor
import com.acer.example.favoritmovie.DatabaseContract.FavColumns.Companion.DESKRIPSI
import com.acer.example.favoritmovie.DatabaseContract.FavColumns.Companion.JUDUL
import com.acer.example.favoritmovie.DatabaseContract.FavColumns.Companion.POSTER
import com.acer.example.favoritmovie.DatabaseContract.FavColumns.Companion._ID

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Movie> {
        val notesList = ArrayList<Movie>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val judul = getString(getColumnIndexOrThrow(JUDUL))
                val deskripsi = getString(getColumnIndexOrThrow(DESKRIPSI))
                val poster = getString(getColumnIndexOrThrow(POSTER))
                notesList.add(Movie(id, judul, deskripsi, poster))
            }
        }
        return notesList
    }

    fun mapCursorToArrayListTvShow(notesCursor: Cursor?): ArrayList<TvShow> {
        val notesList = ArrayList<TvShow>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val judul = getString(getColumnIndexOrThrow(JUDUL))
                val deskripsi = getString(getColumnIndexOrThrow(DESKRIPSI))
                val poster = getString(getColumnIndexOrThrow(POSTER))
                notesList.add(TvShow(id, judul, deskripsi, poster))
            }
        }
        return notesList
    }

    fun mapCursorToObject(notesCursor: Cursor?): Movie {
        var movie = Movie(1,"", "", "")
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavColumns._ID))
            val title = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.JUDUL))
            val description = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.DESKRIPSI))
            val poster = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.POSTER))
            movie = Movie(id, title, description, poster)
        }
        return movie
    }

}