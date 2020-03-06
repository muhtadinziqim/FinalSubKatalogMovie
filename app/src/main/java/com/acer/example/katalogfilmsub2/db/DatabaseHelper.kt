package com.acer.example.katalogfilmsub2.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.acer.example.katalogfilmsub2.Movie
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.DESKRIPSI
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.JUDUL
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.KATEGORI
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.POSTER
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion._ID

internal class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {
        private const val DATABASE_NAME = "dbmovieapp"
        private const val DATABASE_VERSION = 2
        private val SQL_CREATE_TABLE_FAV = "CREATE TABLE $TABLE_NAME" +
            " (${_ID} INTEGER PRIMARY KEY," +
            " ${JUDUL} TEXT NOT NULL," +
            " ${DESKRIPSI} TEXT NOT NULL," +
            " ${POSTER} TEXT NOT NULL,"+
            " ${KATEGORI} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAV)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    //COBA
    fun addMovieFav(id: Int, judul: String, deskripsi: String, poster: String, kategori: String): Long {
        val db = this.writableDatabase
        // Creating content values
        val values = ContentValues()
        values.put(_ID, id)
        values.put(JUDUL, judul)
        values.put(DESKRIPSI, deskripsi)
        values.put(POSTER, poster)
        values.put(KATEGORI, kategori)
        // insert row in students table

        return db.insert(TABLE_NAME, null, values)
    }

    fun deleteFav(id: Int) {

        // delete row in students table based on id
        val db = this.writableDatabase
        db.delete(
            TABLE_NAME, "$_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun queryById(id: Int): ArrayList<Movie> {
        var model = ArrayList<Movie>()
        val db = this.readableDatabase
        var d =  db.query(
            TABLE_NAME,
            null,
            "$_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null)
        if (d.moveToFirst()){
            do {
                val movie = Movie(d.getInt(d.getColumnIndex(_ID)), d.getString(d.getColumnIndex(JUDUL)), d.getString(d.getColumnIndex(
                    DESKRIPSI)), d.getString(d.getColumnIndex(POSTER)))
                model.add(movie)
            }while (d.moveToNext())
        }
        return model
    }
}